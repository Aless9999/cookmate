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
import com.macnigor.cookmate.dto.IngredientDto;
import com.macnigor.cookmate.dto.RecipeJsonDto;
import com.macnigor.cookmate.entity.Ingredient;
import com.macnigor.cookmate.entity.Recipe;
import com.macnigor.cookmate.entity.RecipeIngredient;
import com.macnigor.cookmate.repositories.IngredientRepository;
import com.macnigor.cookmate.repositories.RecipeIngredientRepository;
import com.macnigor.cookmate.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
//@Component
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
            List<RecipeJsonDto> recipeDtos = objectMapper.readValue(inputStream, new TypeReference<>() {});

            for (RecipeJsonDto dto : recipeDtos) {
                // Создаём рецепт
                Recipe recipe = new Recipe();
                recipe.setTitle(dto.title());
                recipe.setDescription(dto.description());
                recipe.setInstructions(dto.instructions());
                recipe.setImageUrl(dto.imageUrl());

                recipeRepository.save(recipe); // Сохраняем сначала, чтобы получить ID для связи

                // Обрабатываем ингредиенты
                for (IngredientDto ingredientDTO : dto.ingredients()) {
                    // Ищем ингредиент по имени или создаём новый
                    Ingredient ingredient = ingredientRepository.findByName(ingredientDTO.name())
                            .orElseGet(() -> {
                                Ingredient newIng = new Ingredient();
                                newIng.setName(ingredientDTO.name());
                                return ingredientRepository.save(newIng);
                            });

                    // Создаём связь RecipeIngredient
                    RecipeIngredient recipeIngredient = new RecipeIngredient();
                    recipeIngredient.setRecipe(recipe);
                    recipeIngredient.setIngredient(ingredient);
                    recipeIngredient.setAmount(ingredientDTO.amount()); // "300 г", "1 шт."
                    recipeIngredientRepository.save(recipeIngredient);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error importing recipes from JSON", e);
        }
    }
}
