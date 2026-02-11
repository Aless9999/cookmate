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

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO для создания или обновления рецепта")
public record RecipeJsonDto(
        @Schema(description = "Название блюда", example = "Борщ", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "Краткое описание", example = "Традиционный суп")
        String description,

        @Schema(description = "Список шагов приготовления", example = "[\"Вскипятить воду\", \"Добавить свеклу\"]")
        List<String> instructions,

        @Schema(description = "Ссылка на изображение", example = "https://example.com/borsch.jpg")
        String imageUrl,

        @Schema(description = "Список необходимых ингредиентов")
        List<IngredientDto> ingredients
) {
    public RecipeJsonDto {
        instructions = instructions == null ? List.of() : List.copyOf(instructions);
        ingredients = ingredients == null ? List.of() : List.copyOf(ingredients);
    }
}

