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

import java.time.Instant;

public record ApiResponse<T>(
        String message,
        int status,
        T data,
        Instant timestamp

) {
    public ApiResponse(String message, int status, T data) {
        this(message, status, data, null);
    }

    public ApiResponse(String message) {
        this(message, 0, null, null);
    }
}
