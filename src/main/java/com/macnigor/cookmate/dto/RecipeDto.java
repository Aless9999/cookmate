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

import com.macnigor.cookmate.entity.Ingredient;
import com.macnigor.cookmate.entity.Recipe;
import com.macnigor.cookmate.entity.RecipeIngredient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j  // Аннотация для логирования
public record RecipeDto(
        Long id,
        String title,
        String description,
        List<String> instructions,
        String imageUrl,
        List<String> ingredients
) {

    // Метод для преобразования из сущности в DTO
    public static RecipeDto fromEntity(Recipe recipe) {
        log.debug("Преобразование сущности Recipe в RecipeDto для id: {}", recipe.getId());

        // Логирование ингредиентов
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

    // Метод для преобразования DTO в сущность
    public Recipe toEntity() {
        log.debug("Преобразование RecipeDto в сущность Recipe для id: {}", this.id);

        Recipe recipe = new Recipe();
        recipe.setTitle(this.title);
        recipe.setDescription(this.description);
        recipe.setInstructions(this.instructions);
        recipe.setImageUrl(this.imageUrl);

        List<RecipeIngredient> recipeIngredients = this.ingredients.stream()
                .map(name -> {
                    log.debug("Обрабатывается ингредиент для создания RecipeIngredient: {}", name);
                    RecipeIngredient recipeIngredient = new RecipeIngredient();
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(name);
                    recipeIngredient.setIngredient(ingredient);
                    recipeIngredient.setRecipe(recipe);
                    return recipeIngredient;
                }).toList();

        recipe.setRecipeIngredients(recipeIngredients);

        log.debug("Преобразование завершено для id: {}", this.id);
        return recipe;
    }
}
