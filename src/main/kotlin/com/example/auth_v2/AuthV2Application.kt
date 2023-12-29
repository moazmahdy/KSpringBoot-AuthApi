package com.example.auth_v2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class AuthV2Application

fun main(args: Array<String>) {
	runApplication<AuthV2Application>(*args)
}