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
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;


public class AddUserOnStartup implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
     private final UserService userService;

    public AddUserOnStartup(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        String username = "admin";
        String password = "100";
        String email = "asd@mail.com";
        String encryptedPassword = passwordEncoder.encode(password);
        UserRegisterDto userRegisterDto = new UserRegisterDto(username,password,email);

        userService.createNewUser(userRegisterDto);
        // Здесь нужно внедрить репозиторий для сохранения пользователя в БД
        // Пример для добавления пользователя:
        // userRepository.save(new User(username, encryptedPassword, "ROLE_ADMIN"));

        System.out.println("Admin user created with password: " + encryptedPassword);
    }
}
