package com.macnigor.cookmate.controllers;


import com.macnigor.cookmate.dto.AuthRequest;
import com.macnigor.cookmate.dto.AuthResponse;
import com.macnigor.cookmate.dto.RegisterResponse;
import com.macnigor.cookmate.dto.UserRegisterDto;
import com.macnigor.cookmate.security.JwtTokenFactory;
import com.macnigor.cookmate.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenFactory jwtTokenFactory;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenFactory jwtTokenFactory) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenFactory = jwtTokenFactory;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        if (request == null || request.username() == null || request.username().isBlank()
                || request.password() == null || request.password().isBlank()) {
            log.warn("Неверный запрос для аутентификации");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        log.info("Попытка входа пользователя с именем: {}", request.username());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));

            String token = jwtTokenFactory.createAccessToken(request.username());
            AuthResponse response;
            response = new AuthResponse(token);
            log.info("Пользователь {} успешно аутентифицирован и токен сгенерирован", request.username());
            return ResponseEntity.ok(response);

        } catch (AuthenticationException ex) {
            log.warn("Аутентификация не прошла для пользователя {}: {}", request.username(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody UserRegisterDto userRegisterDto) {
        log.info("Попытка регистрации пользователя с именем: {}", userRegisterDto.username());

        // Регистрация нового пользователя
        userService.createNewUser(userRegisterDto);
        log.info("Пользователь {} успешно зарегистрирован", userRegisterDto.username());

        RegisterResponse response = new RegisterResponse("Вы успешно зарегистрировались");

        return ResponseEntity.ok(response);
    }
}