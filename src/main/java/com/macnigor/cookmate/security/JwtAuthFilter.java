package com.macnigor.cookmate.security;

import com.macnigor.cookmate.entity.User;
import com.macnigor.cookmate.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Получаем заголовок Authorization
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        log.info("Запрос к URL: {}", request.getRequestURI());

        // Проверяем наличие токена и префикса Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // убираем "Bearer "
            try {
                username = jwtTokenProvider.getUsername(token);
                log.info("JWT токен найден, username из токена: {}", username);
            } catch (Exception e) {
                log.warn("Невалидный JWT токен: {}", e.getMessage());
            }
        } else {
            log.info("Authorization header отсутствует или не начинается с 'Bearer '");
        }

        // Проверяем, что пользователь ещё не аутентифицирован в контексте
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Ищем пользователя в базе
                String finalUsername = username;
                User user = userRepository.findUserByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Пользователь '" + finalUsername + "' не найден"));

                // Проверяем токен через JwtTokenProvider
                if (jwtTokenProvider.validateAccessToken(token)) {
                    // Создаём объект UserDetails для Spring Security
                    UserDetails userDetails = org.springframework.security.core.userdetails.User
                            .withUsername(user.getUsername())
                            .password(user.getPassword())
                            .authorities("USER") // можно расширить ролями из базы
                            .build();

                    // Создаём объект аутентификации и помещаем его в SecurityContext
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.info("Пользователь '{}' успешно аутентифицирован через JWT", username);
                } else {
                    log.warn("JWT токен не прошёл валидацию для пользователя '{}'", username);
                }
            } catch (UsernameNotFoundException e) {
                log.warn(e.getMessage());
            }
        } else if (username != null) {
            log.info("Пользователь '{}' уже аутентифицирован в контексте", username);
        }

        // Продолжаем цепочку фильтров
        filterChain.doFilter(request, response);
    }
}
