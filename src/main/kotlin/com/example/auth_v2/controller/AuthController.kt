package com.example.auth_v2.controller

import com.example.auth_v2.domain.model.User
import com.example.auth_v2.domain.model.dto.LoginDto
import com.example.auth_v2.domain.model.dto.Message
import com.example.auth_v2.domain.model.dto.RegisterDto
import com.example.auth_v2.services.UserServices
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("api")
class AuthController(
    private val services: UserServices
) {

    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("register")
    fun register(
        @RequestBody body: RegisterDto
    ): ResponseEntity<User> {
        val user = User.create(
            name = body.name,
            email = body.email,
            password = body.password
        )

        return ResponseEntity.ok(services.save(user))
    }

    @PostMapping("login")
    fun login(
        @RequestBody body: LoginDto, response: HttpServletResponse
    ): ResponseEntity<Any> {
        val user = services.findByEmail(body.email)
            ?: return ResponseEntity.badRequest().body(Message("user not found"))

        if (!user.comparePassword(body.password)) {
            return ResponseEntity.badRequest().body(Message("invalid password"))
        }

        val issuer = user.id.toString()

        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 1 day
            .signWith(SignatureAlgorithm.HS512, "secret").compact()

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("success"))
    }

    @GetMapping("user")
    fun user(@CookieValue("jwt") jwt: String?): ResponseEntity<Any> {
        try {
            if (jwt == null) {
                return ResponseEntity.status(401).body(Message("Un Auth"))
            }

            val body = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body

            return ResponseEntity.ok(services.getById(body.issuer.toInt()))
        } catch (e: Exception) {
            return ResponseEntity.status(401).body(Message(e.localizedMessage.toString()))
        }
    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {

        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0
        response.addCookie(cookie)
        return ResponseEntity.ok(Message("success"))

    }
}