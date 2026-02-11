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

import com.macnigor.cookmate.dto.RecipeJsonDto;
import com.macnigor.cookmate.entity.Ingredient;
import com.macnigor.cookmate.entity.Recipe;
import com.macnigor.cookmate.entity.RecipeIngredient;
import com.macnigor.cookmate.entity.RecipeInstructions;
import com.macnigor.cookmate.indexes.RecipeSearchIndex;
import com.macnigor.cookmate.repositories.IngredientRepository;
import com.macnigor.cookmate.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeCommandService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeSearchIndex searchIndex;

    @Transactional
    public void create(RecipeJsonDto dto) {

        Recipe recipe = assembleRecipe(dto);
        Recipe saved = recipeRepository.save(recipe);


        searchIndex.updateIndexes(saved);
    }

    private Recipe assembleRecipe(RecipeJsonDto dto) {
        List<RecipeInstructions> instructions = dto.instructions().stream()
                .map(text -> new RecipeInstructions(null, text, 0))
                .toList();

        Set<RecipeIngredient> ingredients = dto.ingredients().stream()
                .map(ing -> {
                    Ingredient ingredient = ingredientRepository.findByName(ing.name())
                            .orElseGet(() -> ingredientRepository.save(new Ingredient(null, ing.name())));
                    return new RecipeIngredient(null, ingredient.id(), ing.amount());
                }).collect(Collectors.toSet());

        return new Recipe(null, dto.title(), dto.description(), dto.imageUrl(), instructions, ingredients);
    }
}

