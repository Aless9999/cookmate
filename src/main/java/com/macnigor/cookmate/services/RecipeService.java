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

import com.macnigor.cookmate.dto.RecipeDto;
import com.macnigor.cookmate.dto.RecipeMatchDto;
import com.macnigor.cookmate.entity.Recipe;
import com.macnigor.cookmate.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeMatchDto> searchByIngredients(List<String> userIngredients) {
        List<String> normalized = userIngredients.stream()
                .map(String::toLowerCase)
                .map(String::trim)
                .toList();

        return recipeRepository.findAll().stream()
                .map(recipe -> {
                    List<String> recipeIngredients = recipe.getRecipeIngredients().stream()
                            .map(ri -> ri.getIngredient().getName().toLowerCase())
                            .toList();

                    long matches = recipeIngredients.stream()
                            .filter(normalized::contains)
                            .count();

                    double score = (double) matches / recipeIngredients.size();

                    return new RecipeMatchDto(RecipeDto.fromEntity(recipe), score);
                })
                .sorted(Comparator.comparingDouble(RecipeMatchDto::score).reversed())
                .toList();
    }


}

