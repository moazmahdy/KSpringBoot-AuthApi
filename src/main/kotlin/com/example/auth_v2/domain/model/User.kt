package com.example.auth_v2.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0,

    @Column
    var name: String,

    @Column(unique = true)
    var email: String,

    @Column
    @JsonIgnore
    var password: String
) {
    companion object {
        private val passwordEncoder = BCryptPasswordEncoder()

        fun create(name: String, email: String, password: String): User {
            return User(name = name, email = email, password = passwordEncoder.encode(password))
        }
    }

    fun comparePassword(password: String): Boolean {
        return BCryptPasswordEncoder().matches(password, this.password)
    }
}