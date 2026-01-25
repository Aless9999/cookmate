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

import com.macnigor.cookmate.entity.User;
import com.macnigor.cookmate.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
@Lazy
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        log.debug("Loading user by username: {}", username);

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Username '" + username + "' not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_USER") // TODO load roles from DB
                .build();
    }
}
