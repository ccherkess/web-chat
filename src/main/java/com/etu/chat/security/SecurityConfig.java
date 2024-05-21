package com.etu.chat.security;

import com.etu.chat.security.filter.GetCsrfTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

@Configuration
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();

        http
            .addFilterAfter(new GetCsrfTokenFilter(), ExceptionTranslationFilter.class)
            .csrf(csrf -> csrf.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler())
                    .csrfTokenRepository(csrfTokenRepository)
                    .sessionAuthenticationStrategy(new CsrfAuthenticationStrategy(csrfTokenRepository))
                    .ignoringRequestMatchers("/api/**")
            )
            .authorizeHttpRequests((requests) -> requests
                    .requestMatchers("/register/**").anonymous()
                    .requestMatchers("/api/room").authenticated()
                    .requestMatchers("/api/room/info/{id:\\d+}").access(getWebExpressionAuthorizationManager(SecurityExpression.ROOM_INFO))
                    .requestMatchers("/api/room/**").hasAuthority("ROLE_ADMIN")
                    .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults());

        return http.build();
    }

}