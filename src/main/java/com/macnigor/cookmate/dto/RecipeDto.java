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

import com.macnigor.cookmate.entity.Recipe;

import java.util.List;

public record RecipeDto(
        Long id,
        String title,
        String description,
        List<String> instructions,
        String imageUrl,
        List<String> ingredients
) {
    public static RecipeDto fromEntity(Recipe recipe) {
        List<String> ingredients = recipe.getRecipeIngredients().stream()
                .map(ri -> ri.getIngredient().getName() +
                        (ri.getAmount() != null ? " (" + ri.getAmount() + ")" : ""))
                .toList();

        return new RecipeDto(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getInstructions(),
                recipe.getImageUrl(),
                ingredients
        );
    }
}

