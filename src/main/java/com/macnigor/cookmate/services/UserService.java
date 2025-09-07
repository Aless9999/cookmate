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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User createNewUser(UserRegisterDto userRegisterDto) {
        try {
            // Логируем начало регистрации
            log.info("Attempting to register user: {}", userRegisterDto.getUsername());

            // Создаем нового пользователя
            User newUser = new User();
            newUser.setUsername(userRegisterDto.getUsername());
            newUser.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
            newUser.setEmail(userRegisterDto.getEmail());

            // Сохраняем в базе
            userRepository.save(newUser);

            // Логируем успешную регистрацию
            log.info("User {} registered successfully", newUser.getUsername());

            return newUser;
        } catch (DataAccessException e) {
            log.error("Database error while trying to register user: {}", userRegisterDto.getUsername(), e);
            throw new RuntimeException("Database error occurred during registration", e);
        } catch (Exception e) {
            log.error("Unexpected error while trying to register user: {}", userRegisterDto.getUsername(), e);
            throw new RuntimeException("Unexpected error occurred during registration", e);
        }
    }

    public boolean login(String username, String rawPassword) {

        return findUserByUsername(username)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword())) // сравнение хеша
                .orElse(false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);

        try {
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));

            log.info("User {} loaded successfully", user.getUsername());

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    new ArrayList<>());

        } catch (DataAccessException e) {
            log.error("Database access error while loading user by username: {}", username, e);
            throw new RuntimeException("Error accessing user data", e); // или другое подходящее исключение
        } catch (Exception e) {
            log.error("Unexpected error while loading user by username: {}", username, e);
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }

}

