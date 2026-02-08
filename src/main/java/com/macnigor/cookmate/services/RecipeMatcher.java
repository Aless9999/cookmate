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
import com.macnigor.cookmate.indexes.RecipeSearchIndex;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RecipeMatcher {

    private final RecipeSearchIndex searchIndex;

    /**
     * Превращает сущность Recipe в Match DTO с расчетом покрытия
     */
    public RecipeMatchDto createMatchDto(Recipe recipe, Set<Long> userIngredientIds) {
        Set<Long> recipeIngredientIds = searchIndex.getIngredientIdsByRecipeId(recipe.id());
        int total = recipeIngredientIds.size();

        long matched = recipeIngredientIds.stream()
                .filter(userIngredientIds::contains)
                .count();

        double coverage = total > 0 ? (double) matched / total : 0.0;

        return new RecipeMatchDto(
                RecipeDto.fromEntity(recipe, searchIndex.getAllIngredientsById()),
                matched,
                coverage
        );
    }

    /**
     * Компаратор для сортировки: Сначала по проценту, потом по количеству
     */
    public Comparator<RecipeMatchDto> getRecipeComparator() {
        return Comparator.comparingDouble(RecipeMatchDto::coveragePercent)
                .thenComparingLong(RecipeMatchDto::matchCount);
    }
}
