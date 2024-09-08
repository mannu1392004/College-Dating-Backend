package com.example.collegeDate.security.jwtutil

import com.example.collegeDate.model.Tokens
import com.example.collegeDate.service.database.DatabaseOperations
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil @Autowired constructor(
    private val databaseOperations: DatabaseOperations
) {
    private val secretKey = "Your Secret Key"
    val key = Keys.hmacShaKeyFor(secretKey.toByteArray())


    fun getTokens(userName: String): Tokens {
        return Tokens(
            accessToken = generateAccessToken(userName),
            refreshToken = generateRefreshToken(userName),
            status = "Good"
        )
    }


    fun generateEmailToken(email: String): String {
        val now = Date()
        val expiryDate = Date(now.time +  3600000) // 1hr minutes
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(expiryDate)
            .setIssuer("e")
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }


    private fun generateAccessToken(userName: String): String {
        val now = Date()
        val expiryDate =Date(now.time + 86400000) // 24 hours
        return Jwts.builder()
            .setSubject(userName)
            .setIssuedAt(Date())
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .setIssuer("a")
            .compact()
    }

    private fun generateRefreshToken(userName: String): String {
        val now = Date()
        val expiryDate = Date(now.time + 2592000000L) // 30 days
        return Jwts.builder()
            .setSubject(userName)
            .setIssuedAt(Date())
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .setIssuer("r")
            .compact()

    }


    fun extractClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun extractUserName(token: String): String {
        return extractClaims(token).subject
    }

    fun isTokenExpired(token: String): Boolean {
        return extractClaims(token).expiration.before(Date())
    }


    fun validRefreshToken(token: String): Boolean {
        return !isTokenExpired(token) && (extractClaims(token).issuer == "r")
    }


    fun validToken(token: String, userName: String): Boolean {
        val extractClaims = extractClaims(token)

        if (extractClaims.issuer == "a") {
        return    !isTokenExpired(token)
        } else if (extractClaims.issuer == "e") {

          val out = !isTokenExpired(token) && databaseOperations.invalidCheck(token)
          databaseOperations.addInvalidToken(token)
           return out
        } else {
         return   false
        }
    }
}