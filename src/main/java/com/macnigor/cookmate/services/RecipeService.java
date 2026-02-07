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
import com.macnigor.cookmate.entity.Ingredient;
import com.macnigor.cookmate.entity.Recipe;
import com.macnigor.cookmate.entity.RecipeIngredient;
import com.macnigor.cookmate.repositories.IngredientRepository;
import com.macnigor.cookmate.repositories.RecipeRepository;
import com.macnigor.cookmate.utils.IngredientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Slf4j
@Service
public class RecipeService {
   private final RecipeRepository recipeRepository;
   private final IngredientRepository ingredientRepository;

    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }


    // Метод для поиска рецептов по ингредиентам
    public List<RecipeMatchDto> searchByIngredients(List<String> userIngredients) {
        log.debug("Поиск по ингредиентам: {}", userIngredients);

        // 1. Подготовка данных
        List<String> normalizedIngredients = normalizeIngredients(userIngredients);
        List<Ingredient> ingredientList = StreamSupport.stream(ingredientRepository.findAll().spliterator(),false).toList();
        List<Ingredient>userIngredient = ingredientList.stream()
                .filter(ing->ing.name().equals(normalizedIngredients.stream()
                        .))
        // 2. Получение кандидатов из БД
        List<Long> ids = recipeRepository.findRecipeIdsByIngredients(normalizedIngredients)
                .stream()
                .distinct()
                .toList();

        List<Recipe> candidates = ingredientList.stream
                .map(ingr->{
                    recipeRepository.findAllById(ingr.)
                }
        ).toList();
        // 3. Обработка, расчет метрик и сортировка
        List<RecipeMatchDto> results = candidates.stream()
                .map(recipe -> createMatchDto(recipe, normalizedIngredients))
                // Сортировка: сначала по проценту покрытия (кто ближе к 100%), затем по количеству совпадений
                .sorted(Comparator.comparingDouble(RecipeMatchDto::coveragePercent).reversed()
                        .thenComparingLong(RecipeMatchDto::matchCount).reversed())
                .collect(Collectors.toList());

        log.debug("Найдено {} рецептов после фильтрации.", results.size());
        return results;
    }


    private List<String> normalizeIngredients(List<String> ingredients) {
        if (ingredients == null) return List.of();
        return ingredients.stream()
                .map(String::toLowerCase)
                .map(String::trim)
                .distinct() // Убираем дубликаты в запросе, если пользователь ввел "яйцо" дважды
                .toList();
    }


    private RecipeMatchDto createMatchDto(Recipe recipe, List<String> userIngredients) {
        Set<RecipeIngredient> recipeIngredients = recipe.ingredients();


        Map<Long, Ingredient> ingredientMap = StreamSupport.stream(
                ingredientRepository.findAllById(
                        recipeIngredients.stream()
                                .map(RecipeIngredient::ingredientId)
                                .toList()
                ).spliterator(), false
        ).collect(Collectors.toMap(Ingredient::id, Function.identity()));

        List<RecipeIngredient> matched = recipeIngredients.stream()
                .filter(ri -> {
                    Ingredient ing = ingredientMap.get(ri.ingredientId());
                    return ing != null && userIngredients.contains(ing.name().toLowerCase());
                })
                .toList();


        long matchCount = matched.size();
        int totalIngredients = recipeIngredients.size();

        // Считаем покрытие (защита от деления на 0, если рецепт пустой)
        double coverage = totalIngredients > 0 ? (double) matchCount / totalIngredients : 0.0;

        // Конвертируем веса (твоя логика сохранена)
        List<Integer> amounts = matched.stream()
                .map(ri -> IngredientUtils.parseAndConvertAmount(ri.amount()))
                .toList();

        return new RecipeMatchDto(
                RecipeDto.fromEntity(recipe,ingredientMap),
                matchCount,
                coverage,
                amounts
        );
    }
}