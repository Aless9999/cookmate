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

import java.util.List;

public record RecipeJsonDto(
        String title,
        String description,
        List<String> instructions,
        String imageUrl,
        List<IngredientDto> ingredients
) {
    public RecipeJsonDto {
        instructions = instructions == null ? List.of() : List.copyOf(instructions);
        ingredients = ingredients == null ? List.of() : List.copyOf(ingredients);
    }
}

