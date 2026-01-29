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

import com.macnigor.cookmate.projection.RecipeView;
import com.macnigor.cookmate.entity.Recipe;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public record RecipeDto (
        Long id,
        String title,
        String description,
        List<String> instructions,
        String imageUrl,
        List<String> ingredients
) implements RecipeView{
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public List<String> getInstructions() {
        return this.instructions;
    }

    @Override
    public String getImageUrl() {
        return this.imageUrl;
    }

    @Override
    public List<String> getIngredientsList() {
        return this.ingredients;
    }

    public static RecipeDto fromEntity(Recipe recipe) {
        log.debug("Преобразование сущности Recipe в RecipeDto для id: {}", recipe.getId());

        List<String> ingredients = recipe.getRecipeIngredients().stream()
                .map(ri -> {
                    String ingredientName = ri.getIngredient().getName();
                    String amount = ri.getAmount() != null ? " (" + ri.getAmount() + ")" : "";
                    log.debug("Обрабатывается ингредиент: {}{}", ingredientName, amount);
                    return ingredientName + amount;
                })
                .toList();

        RecipeDto recipeDto = new RecipeDto(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getInstructions(),
                recipe.getImageUrl(),
                ingredients
        );
        log.debug("Преобразование завершено для id: {}", recipe.getId());
        return recipeDto;
    }
}
