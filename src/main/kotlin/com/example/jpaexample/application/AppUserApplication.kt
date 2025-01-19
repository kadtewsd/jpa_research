package com.example.jpaexample.application

import com.example.jpaexample.domain.model.AppUser
import com.example.jpaexample.domain.model.AppUserRepository
import com.example.jpaexample.domain.model.UserId
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class AppUserApplication(
    private val repository: AppUserRepository,
) {
    fun create(userName: String): AppUser = repository.addOrUpdate(
        appUser = AppUser(
            name = userName,
        )
    )
}
