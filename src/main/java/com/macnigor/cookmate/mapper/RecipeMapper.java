/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.mapper;

import com.macnigor.cookmate.dto.RecipeDto;
import com.macnigor.cookmate.entity.Ingredient;
import com.macnigor.cookmate.entity.Recipe;
import com.macnigor.cookmate.entity.RecipeIngredient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RecipeMapper {

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



    public static Recipe toEntity(RecipeDto recipeDto) {
        log.debug("Преобразование RecipeDto в сущность Recipe для id: {}", recipeDto.id());

        Recipe recipe = new Recipe();
        recipe.setTitle(recipeDto.title());
        recipe.setDescription(recipeDto.description());
        recipe.setInstructions(recipeDto.instructions());
        recipe.setImageUrl(recipe.getImageUrl());

        List<RecipeIngredient> recipeIngredients = recipeDto.ingredients().stream()
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

        log.debug("Преобразование завершено для id: {}", recipeDto.id());
        return recipe;
    }
}
