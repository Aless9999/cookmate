/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.controllers;

import com.macnigor.cookmate.dto.ApiResponse;
import com.macnigor.cookmate.dto.UserRegisterDto;
import com.macnigor.cookmate.dto.UserLoginDto;
import com.macnigor.cookmate.entity.User;
import com.macnigor.cookmate.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        log.info("UserController initialized");
    }

    // Регистрация
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        log.info("Попытка регистрации нового пользователя с именем: {}", userRegisterDto.getUsername());
        userService.createNewUser(userRegisterDto);
        log.info("Пользователь {} успешно зарегистрирован", userRegisterDto.getUsername());
        return ResponseEntity.ok("User with username " + userRegisterDto.getUsername() + " registered");
    }

    // Логин
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@Valid @RequestBody UserLoginDto loginDto) {
        log.info("Попытка входа пользователя: {}", loginDto.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            log.debug("Authentication object: {}", authentication);
            log.info("Пользователь {} аутентифицирован", loginDto.getUsername());

            return ResponseEntity.ok(new ApiResponse("Login successful for " + loginDto.getUsername()));
        } catch (AuthenticationException ex) {
            log.warn("Ошибка входа {}. Reason: {}", loginDto.getUsername(), ex.getMessage());
            return ResponseEntity.status(401).body(new ApiResponse("Invalid username or password"));
        } catch (Exception ex) {
            log.error("Не найден пользователь {}", loginDto.getUsername(), ex);
            return ResponseEntity.status(500).body(new ApiResponse("Unexpected error occurred"));
        }
    }
}
