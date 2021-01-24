package com.texas.holdem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * konfiguracja message brokera<br/>
     * włączenie wysyłania wiadomości na prefix /topic
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
    }

    /**
     * konfiguracja corsa dla socketów
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/room/{roomId}").setAllowedOrigins("https://exsfromtexas.netlify.app", "http://localhost:3000").withSockJS();
        registry.addEndpoint("/room/{roomId}").setAllowedOrigins("https://exsfromtexas.netlify.app", "http://localhost:3000");

    }

}
