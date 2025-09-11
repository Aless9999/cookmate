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
public class RecipeMessageDto {
  private  String message; // сообщение которое получает клиент
    private String imageUrl; // ссылка на изображение

    public RecipeMessageDto(String message, String imageUrl) {
        this.message = message;
        this.imageUrl = imageUrl;
    }
}
