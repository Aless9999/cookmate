/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.services;

import com.macnigor.cookmate.dto.UserRegisterDto;
import com.macnigor.cookmate.entity.User;
import com.macnigor.cookmate.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }


    @Transactional
    public User createNewUser(UserRegisterDto userRegisterDto) {
        try {
            log.info("Начало регистрации пользователя: {}", userRegisterDto.username());

            // Проверка на существующего пользователя
            if (userRepository.existsByUsername(userRegisterDto.username())) {
                log.warn("Пользователь с именем '{}' уже существует", userRegisterDto.username());
                throw new IllegalArgumentException("Имя пользователя уже занято");
            }

            if (userRepository.existsByEmail(userRegisterDto.email())) {
                log.warn("Пользователь с email '{}' уже существует", userRegisterDto.email());
                throw new IllegalArgumentException("Email уже используется");
            }

            // Создание нового пользователя
            User newUser = new User();
            newUser.setUsername(userRegisterDto.username());
            newUser.setPassword(passwordEncoder.encode(userRegisterDto.password()));
            newUser.setEmail(userRegisterDto.email());

            // Сохраняем в базе
            userRepository.save(newUser);

            log.info("Пользователь '{}' успешно зарегистрирован", newUser.getUsername());

            return newUser;
        } catch (DataAccessException e) {
            log.error("Ошибка базы данных при регистрации пользователя '{}'", userRegisterDto.username(), e);
            throw new RuntimeException("Ошибка базы данных при регистрации пользователя", e);
        } catch (Exception e) {
            log.error("Неожиданная ошибка при регистрации пользователя '{}'", userRegisterDto.username(), e);
            throw new RuntimeException("Неожиданная ошибка при регистрации пользователя", e);
        }
    }







}

