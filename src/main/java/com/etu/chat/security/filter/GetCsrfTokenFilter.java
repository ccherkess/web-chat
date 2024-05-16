package com.etu.chat.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/*
for front
* fetch("/csrf")
    .then(response => response.json())
    .then(csrf => {
        const headers = {};
        headers[csrf.headerName] = xorWithRandomBytes(csrf.token);

        return fetch("/change-password", {
            method: "POST",
            headers: headers
            // прочий код
        })
    })
*/
public class GetCsrfTokenFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/csrf", HttpMethod.GET.name());

    @Setter
    private CsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
    @Setter
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)) {
            var token = this.csrfTokenRepository.loadToken(request);

            if (token == null) {
                token = this.csrfTokenRepository.generateToken(request);
                this.csrfTokenRepository.saveToken(token, request, response);
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            this.objectMapper.writeValue(response.getOutputStream(), token);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
