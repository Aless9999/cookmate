/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.facade;

import com.macnigor.cookmate.services.RecipeMatcher;
import com.macnigor.cookmate.dto.RecipeMatchDto;
import com.macnigor.cookmate.repositories.RecipeRepository;
import com.macnigor.cookmate.indexes.RecipeSearchIndex;
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
public class RecipeServiceFacade {

    private final RecipeRepository recipeRepository;
    private final RecipeSearchIndex searchIndex;
    private final RecipeMatcher recipeMatcher;

    public List<RecipeMatchDto> searchByIngredients(List<String> userIngredients) {
        log.debug("Поиск по ингредиентам: {}", userIngredients);

        // 1. Нормализация и получение ID
        Set<Long> userIngredientIds = getUserIngredientIds(userIngredients);
        if (userIngredientIds.isEmpty()) return List.of();

        // 2. Поиск кандидатов через индекс
        Set<Long> candidateRecipeIds = userIngredientIds.stream()
                .flatMap(id -> searchIndex.getRecipeIdsByIngredientId(id).stream())
                .collect(Collectors.toSet());

        if (candidateRecipeIds.isEmpty()) return List.of();

        // 3. Загрузка из БД и финальное ранжирование
        return StreamSupport.stream(recipeRepository.findAllById(candidateRecipeIds).spliterator(), false)
                .map(recipe -> recipeMatcher.createMatchDto(recipe, userIngredientIds))
                .filter(dto -> dto.coveragePercent() > 0)
                .sorted(recipeMatcher.getRecipeComparator()) // Используем наш компаратор
                .toList()
                .reversed(); // Оставляем reversed, как вы просили
    }

    private Set<Long> getUserIngredientIds(List<String> userIngredients) {
        return normalizeIngredients(userIngredients).stream()
                .map(searchIndex::getIngredientIdByName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<String> normalizeIngredients(List<String> ingredients) {
        if (ingredients == null) return Set.of();
        return ingredients.stream()
                .map(String::toLowerCase)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());
    }
}

