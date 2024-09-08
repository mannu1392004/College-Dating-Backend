package com.example.collegeDate.config

import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

class WebsocketConfig( private val jwtHandshakeInterceptor: JwtHandshakeInterceptor) : WebSocketMessageBrokerConfigurer {

        override fun registerStompEndpoints(registry: StompEndpointRegistry) {
            registry.addEndpoint("/chat")
                .addInterceptors(jwtHandshakeInterceptor)  // Register the JWT handshake interceptor
                .setAllowedOrigins("*")
                .withSockJS()
        }

        // Configure other WebSocket settings as needed




}
