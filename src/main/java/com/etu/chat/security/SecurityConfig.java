package com.etu.chat.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApplicationContext applicationContext;

    private WebExpressionAuthorizationManager getWebExpressionAuthorizationManager(SecurityExpression expression) {
        var expressionHandler = new DefaultHttpSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);

        var authorizationManager = new WebExpressionAuthorizationManager(expression.getValue());
        authorizationManager.setExpressionHandler(expressionHandler);

        return authorizationManager;
    }

    private void requestMatchersConfig(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry requests) {
        requests
            .requestMatchers("/registration")
                .anonymous()
            .requestMatchers("/registration/**")
                .hasAuthority("ROLE_ADMIN")
            .requestMatchers("/api/room")
                .authenticated()
            .requestMatchers("/api/room/info/{id:\\d+}")
                .access(getWebExpressionAuthorizationManager(SecurityExpression.ROOM_INFO))
            .requestMatchers("/api/room/**")
                .hasAuthority("ROLE_ADMIN")
            .requestMatchers("/api/messages/{roomId:\\d+}/**")
                .access(getWebExpressionAuthorizationManager(SecurityExpression.ROOM_MESSAGE_READ))
            .requestMatchers("/api/messages/send/{roomId:\\d+}")
                .access(getWebExpressionAuthorizationManager(SecurityExpression.ROOM_MESSAGE_WRITE))
            .requestMatchers("/api/messages/edit/{id:\\d+}", "/api/messages/delete/{id:\\d+}")
                .access(getWebExpressionAuthorizationManager(SecurityExpression.MESSAGE_EDIT))
            .requestMatchers("/api/user/room/not/{roomId:\\d+}")
                .hasAuthority("ROLE_ADMIN")
            .anyRequest().authenticated();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(this::requestMatchersConfig)
            .httpBasic(Customizer.withDefaults())
            .formLogin(login -> login
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll()
            );

        return http.build();
    }

}
