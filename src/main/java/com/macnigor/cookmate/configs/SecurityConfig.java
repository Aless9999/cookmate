package com.macnigor.cookmate.configs;

import com.macnigor.cookmate.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Configuring HTTP security settings...");

        http
                .csrf(csrf -> csrf.disable())  // Отключаем CSRF для REST API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/").permitAll()  // Доступ ко всем для /
                        .requestMatchers("/api/users/register").permitAll()  // Доступ всем для register
                        .requestMatchers("/api/users/login").permitAll() // Доступ всем для login
                        .requestMatchers("/api/recipes/search").permitAll()
                        .anyRequest().authenticated()  // Все остальные страницы требуют аутентификации
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // REST API без сессий
                )
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))  // Для работы с H2 консолью
                .httpBasic(Customizer.withDefaults());  // Включение basic auth

        log.info("HTTP security settings successfully configured.");
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        log.info("Creating AuthenticationManager...");
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        log.info("AuthenticationManager created successfully.");
        return authenticationManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Creating BCryptPasswordEncoder...");
        return new BCryptPasswordEncoder();
    }


}
