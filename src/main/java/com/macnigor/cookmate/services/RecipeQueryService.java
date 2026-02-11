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

import com.macnigor.cookmate.dto.RecipeMatchDto;
import com.macnigor.cookmate.indexes.RecipeSearchIndex;
import com.macnigor.cookmate.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeQueryService {
    private final RecipeRepository recipeRepository;
    private final RecipeSearchIndex searchIndex;
    private final RecipeMatcher recipeMatcher;

    public List<RecipeMatchDto> searchByIngredients(List<String> userIngredients) {
        Set<Long> userIngredientIds = resolveIngredientIds(userIngredients);
        if (userIngredientIds.isEmpty()) return List.of();

        Set<Long> candidateIds = userIngredientIds.stream()
                .flatMap(id -> searchIndex.getRecipeIdsByIngredientId(id).stream())
                .collect(Collectors.toSet());

        return StreamSupport.stream(recipeRepository.findAllById(candidateIds).spliterator(), false)
                .map(recipe -> recipeMatcher.createMatchDto(recipe, userIngredientIds))
                .filter(dto -> dto.coveragePercent() > 0)
                .sorted(recipeMatcher.getRecipeComparator().reversed())
                .toList();
    }

    private Set<Long> resolveIngredientIds(List<String> names) {
        return names.stream()
                .filter(Objects::nonNull)
                .map(name -> searchIndex.getIngredientIdByName(name.trim().toLowerCase()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
