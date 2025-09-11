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

import java.util.List;
import java.util.stream.Collectors;

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

    public Recipe toEntity() {
        Recipe recipe = new Recipe();
        recipe.setTitle(this.title);
        recipe.setDescription(this.description);
        recipe.setInstructions(this.instructions);
        recipe.setImageUrl(this.imageUrl);

        List<RecipeIngredient> recipeIngredients = this.ingredients.stream()
                .map(name -> {
                    RecipeIngredient recipeIngredient = new RecipeIngredient();
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(name);
                    recipeIngredient.setIngredient(ingredient);
                    recipeIngredient.setRecipe(recipe);
                    return recipeIngredient;
                }).toList();
        return recipe;

    }

}

