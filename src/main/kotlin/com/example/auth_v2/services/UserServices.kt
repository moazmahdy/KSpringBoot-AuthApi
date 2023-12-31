package com.example.auth_v2.services

import com.example.auth_v2.domain.model.User
import com.example.auth_v2.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserServices(private val userRepository: UserRepository) {

    fun save(user: User): User = this.userRepository.save(user)

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun getById(id: Int): User {
        return userRepository.getReferenceById(id)
    }
}