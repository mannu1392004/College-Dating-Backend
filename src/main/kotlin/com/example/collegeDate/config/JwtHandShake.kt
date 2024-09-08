package com.example.collegeDate.config
import com.example.collegeDate.security.jwtutil.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Configuration
@EnableWebSecurity
class JwtHandshakeInterceptor(private val jwtTokenProvider: JwtUtil) : HandshakeInterceptor {

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val queryParams = request.uri.query  // Get the query string from the URI
        val token = queryParams?.split("&")?.find { it.startsWith("token=") }?.substringAfter("token=")

        return if (token != null ) {
            val authentication = UsernamePasswordAuthenticationToken("mannu", null, emptyList())
            SecurityContextHolder.getContext().authentication = authentication

           // Attach the authentication to the WebSocket session
            true
        } else {
           // response.setStatusCode(HttpStatus.FORBIDDEN)
            //false
            val authentication = UsernamePasswordAuthenticationToken("mannu", null, emptyList())
            SecurityContextHolder.getContext().authentication = authentication
            true
        }
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: java.lang.Exception?
    ) {

    }
}
