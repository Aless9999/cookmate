/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegisterDto {
    private String username;
    private String password;
    private String email;
}
