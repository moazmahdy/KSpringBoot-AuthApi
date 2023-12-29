package com.example.auth_v2.domain.model.dto

data class RegisterDto (
    val name: String,
    val email:String,
    val password: String
)