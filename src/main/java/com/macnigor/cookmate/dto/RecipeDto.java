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
import com.macnigor.cookmate.entity.RecipeInstructions;
import com.macnigor.cookmate.projection.RecipeView;
import com.macnigor.cookmate.entity.Recipe;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record RecipeDto (
        Long id,
        String title,
        String description,
        List<String> instructions,
        String imageUrl,
        List<String> ingredients
) implements RecipeView {

    private static final Logger log = LoggerFactory.getLogger(RecipeDto.class);

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

    /**
     * Преобразует Recipe в RecipeDto.
     * @param recipe агрегат Recipe
     * @param ingredientsById Map всех ингредиентов по их id
     * @return RecipeDto с именами и количествами ингредиентов
     */
    public static RecipeDto fromEntity(Recipe recipe, Map<Long, Ingredient> ingredientsById) {
        log.debug("Преобразование сущности Recipe в RecipeDto для id: {}", recipe.id());

        List<String> ingredients = recipe.ingredients().stream()
                .map(ri -> {
                    Ingredient ingredient = ingredientsById.get(ri.ingredientId());
                    String ingredientName = ingredient != null ? ingredient.name() : "Неизвестный ингредиент";
                    String amount = ri.amount() != null ? " (" + ri.amount() + ")" : "";
                    log.debug("Обрабатывается ингредиент: {}{}", ingredientName, amount);
                    return ingredientName + amount;
                })
                .toList();

        RecipeDto recipeDto = new RecipeDto(
                recipe.id(),
                recipe.title(),
                recipe.description(),
                recipe.instructions().stream()
                        .map(RecipeInstructions::instruction)
                        .toList(),
                recipe.imageUrl(),
                ingredients
        );

        log.debug("Преобразование завершено для id: {}", recipe.id());
        return recipeDto;
    }
}
