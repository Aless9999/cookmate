/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public record User(
        @Id
        @Column("id") // Явно фиксируем имя, чтобы избежать генерации "ID" или "I"
        Long id,

        @Column("username")
        String username,

        @Column("password")
        String password,

        @Column("email")
        String email
) {
    // Вторичный конструктор для создания нового пользователя
    public User(String username, String password, String email) {
        this(null, username, password, email);
    }
}

