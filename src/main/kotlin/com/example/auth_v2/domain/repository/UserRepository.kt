package com.example.auth_v2.domain.repository

import com.example.auth_v2.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {

    fun findByEmail(email: String): User?
}