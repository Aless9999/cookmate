package com.macnigor.cookmate.controllers;


import com.macnigor.cookmate.dto.AuthRequest;
import com.macnigor.cookmate.dto.AuthResponse;
import com.macnigor.cookmate.dto.UserRegisterDto;
import com.macnigor.cookmate.security.JwtTokenProvider;
import com.macnigor.cookmate.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j  // Аннотация для логирования
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        log.info("Попытка входа пользователя с именем: {}", request.getUsername());  // Логирование входа

        // 1. Аутентифицируем пользователя
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        if (authentication.isAuthenticated()) {
            log.info("Пользователь {} успешно аутентифицирован", request.getUsername());  // Логирование успешной аутентификации
        } else {
            log.warn("Не удалось аутентифицировать пользователя {}", request.getUsername());  // Логирование неудачной аутентификации
        }

        // Создание токена
        String token = jwtTokenProvider.createAccessToken(request.getUsername());
        AuthResponse response = new AuthResponse(token);

        log.info("Токен доступа для пользователя {} успешно сгенерирован", request.getUsername());  // Логирование генерации токена

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDto userRegisterDto) {
        log.info("Попытка регистрации пользователя с именем: {}", userRegisterDto.getUsername());  // Логирование попытки регистрации

        // Регистрация нового пользователя
        userService.createNewUser(userRegisterDto);
        log.info("Пользователь {} успешно зарегистрирован", userRegisterDto.getUsername());  // Логирование успешной регистрации



        return ResponseEntity.ok("Вы успешно зарегистрировались");
    }
}