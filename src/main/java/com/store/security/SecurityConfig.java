package com.store.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@org.springframework.context.annotation.Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("script-src 'self' 'unsafe-inline'; object-src 'none';")))
                .csrf(csrf -> csrf.disable())
                .securityMatcher("/**") // Áp dụng với tất cả các đường dẫn
                .httpBasic(httpBasic -> httpBasic.disable()) // Tắt Basic Authentication
                .formLogin(formLogin -> formLogin.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());
        return http.build();
    }
}
