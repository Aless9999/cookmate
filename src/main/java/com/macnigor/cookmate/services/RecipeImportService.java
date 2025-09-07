/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macnigor.cookmate.dto.IngredientDTO;
import com.macnigor.cookmate.dto.RecipeJsonDTO;
import com.macnigor.cookmate.entity.Ingredient;
import com.macnigor.cookmate.entity.Recipe;
import com.macnigor.cookmate.entity.RecipeIngredient;
import com.macnigor.cookmate.repositories.IngredientRepository;
import com.macnigor.cookmate.repositories.RecipeIngredientRepository;
import com.macnigor.cookmate.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class RecipeImportService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void importRecipesFromJson() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/recipes.json");
            List<RecipeJsonDTO> recipeDtos = objectMapper.readValue(inputStream, new TypeReference<>() {});

            for (RecipeJsonDTO dto : recipeDtos) {
                // Создаём рецепт
                Recipe recipe = new Recipe();
                recipe.setTitle(dto.getTitle());
                recipe.setDescription(dto.getDescription());
                recipe.setInstructions(dto.getInstructions());
                recipe.setImageUrl(dto.getImageUrl());

                recipeRepository.save(recipe); // Сохраняем сначала, чтобы получить ID для связи

                // Обрабатываем ингредиенты
                for (IngredientDTO ingredientDTO : dto.getIngredients()) {
                    // Ищем ингредиент по имени или создаём новый
                    Ingredient ingredient = ingredientRepository.findByName(ingredientDTO.getName())
                            .orElseGet(() -> {
                                Ingredient newIng = new Ingredient();
                                newIng.setName(ingredientDTO.getName());
                                return ingredientRepository.save(newIng);
                            });

                    // Создаём связь RecipeIngredient
                    RecipeIngredient recipeIngredient = new RecipeIngredient();
                    recipeIngredient.setRecipe(recipe);
                    recipeIngredient.setIngredient(ingredient);
                    recipeIngredient.setAmount(ingredientDTO.getAmount()); // "300 г", "1 шт."
                    recipeIngredientRepository.save(recipeIngredient);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error importing recipes from JSON", e);
        }
    }
}
