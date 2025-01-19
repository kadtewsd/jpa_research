package com.example.jpaexample.infrastructure

import com.example.jpaexample.domain.model.AppUser
import com.example.jpaexample.domain.model.UserId
import com.example.jpaexample.domain.model.AppUserRepository
import com.example.jpaexample.domain.model.AppUser_
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserRepositoryImpl(
    private val jpaUserRepository: JpaUserRepository,
) : AppUserRepository {

    override fun addOrUpdate(appUser: AppUser): AppUser {
        return jpaUserRepository.save(appUser)
    }

    override fun get(userId: UserId): AppUser? {
        return jpaUserRepository.findByUserId(userId)
    }

    override fun findBy(userId: UserId): AppUser? {
        return jpaUserRepository.findAll(specOf(userId)).firstOrNull()
    }

    fun specOf(id: UserId): Specification<AppUser> {
        return Specification<AppUser> { root, _, cb ->
            cb.equal(
                root.get(AppUser_.userId),
                id
                // 下記でもコンパイルは通ってしまう。実行時に、ClassCastException. EmbeddedAttributeMapping cannot be cast to class org.hibernate.metamodel.mapping.BasicValuedMapping
//                root.get(AppUser_.userId),
//                id.userId
            )
        }
    }
}

/**
 * infrastructure のレイヤ
 */
interface JpaUserRepository : JpaRepository<AppUser, UUID>, JpaSpecificationExecutor<AppUser> {
    fun findByUserId(userId: UserId): AppUser?
}