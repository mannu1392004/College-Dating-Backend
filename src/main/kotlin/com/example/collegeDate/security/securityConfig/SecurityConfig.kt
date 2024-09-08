package com.example.collegeDate.security.securityConfig

import com.example.collegeDate.security.jwtRequestFilter.JwtRequestFilter
import com.example.collegeDate.security.jwtutil.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Suppress("removal")
@Configuration
@EnableWebSecurity
class SecurityConfig( private val jwtUtil: JwtUtil) {

    @Bean
    fun securityFilterChain(http:HttpSecurity):SecurityFilterChain{
        http.csrf {
            it.disable()
        }.authorizeHttpRequests()
            .requestMatchers("/auth/**", "/public/**","/chat/**").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .addFilterBefore(JwtRequestFilter(jwtUtil), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()

    }
}

