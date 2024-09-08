package com.example.collegeDate.security.jwtRequestFilter

import com.example.collegeDate.security.jwtutil.JwtUtil
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import org.slf4j.LoggerFactory

class JwtRequestFilter(private val jwtUtil: JwtUtil) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(JwtRequestFilter::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")
        var username: String? = null
        var jwtToken: String? = null

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7)
            try {
                username = jwtUtil.extractUserName(jwtToken)
            } catch (e: ExpiredJwtException) {

                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Token expired")
                response.writer.flush()
                return
            } catch (e: Exception) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Invalid token")
                response.writer.flush()
                return
            }
        }

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            if (jwtUtil.validToken(jwtToken!!, username)) {
                val authentication = UsernamePasswordAuthenticationToken(username, null, emptyList())
                SecurityContextHolder.getContext().authentication = authentication
            }
            else if (jwtUtil.validRefreshToken(jwtToken)){
                response.status = HttpServletResponse.SC_OK
                val username1 = jwtUtil.extractUserName(jwtToken)
                val newToken = jwtUtil.getTokens(username1)

                response.writer.write("New token:${newToken.accessToken}  Accessed Token:${newToken.refreshToken}")
            }


            else {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Invalid or expired token")
                response.writer.flush()
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}
