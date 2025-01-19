package com.example.jpaexample

import com.example.jpaexample.application.AppUserApplication
import com.example.jpaexample.domain.model.AppUserRepository
import com.example.jpaexample.domain.model.UserId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
class JpaExampleTests(
	@Autowired private val userApplication: AppUserApplication,
	@Autowired private val userRepository: AppUserRepository,
) {

	@Test
	@DisplayName("データ投入と取得ができる")
	fun testAddAndGet() {
		userApplication.create(
			userName = "test"
		).let {
			val user = userRepository.get(it.userId)
			assertThat(user)
				.describedAs("userId で値が取得できること").isNotNull

			val given = "new_name"

			userRepository.addOrUpdate(user!!.changeName(given))

			userRepository.get(it.userId).let {
				assertThat(it!!.name())
					.describedAs("名前が変更されていること")
					.isEqualTo(given)
			}
		}
	}

	@DisplayName("CriteriaAPI で取得する")
	@Test
	fun getJpaSpecifications() {
		val result = userApplication.create(UUID.randomUUID().toString())
		userRepository.findBy(result.userId)
	}

}
