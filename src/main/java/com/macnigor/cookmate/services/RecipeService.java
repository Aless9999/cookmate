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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j  // Добавляем аннотацию для логирования
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // Метод для поиска рецептов по ингредиентам
    public List<RecipeMatchDto> searchByIngredients(List<String> userIngredients) {
        log.debug("Начало поиска рецептов по ингредиентам. Пользовательские ингредиенты: {}", userIngredients);

        // Нормализуем пользовательские ингредиенты (приводим к нижнему регистру и убираем пробелы)
        List<String> normalized = userIngredients.stream()
                .map(String::toLowerCase)
                .map(String::trim)
                .toList();

        log.debug("Нормализованные ингредиенты пользователя: {}", normalized);

        // Получаем все рецепты из базы данных
        List<RecipeMatchDto> matches = recipeRepository.findAll().stream()
                .map(recipe -> {
                    List<String> recipeIngredients = recipe.getRecipeIngredients().stream()
                            .map(ri -> ri.getIngredient().getName().toLowerCase())
                            .toList();

                    log.debug("Рецепт {}: найдено {} ингредиентов.", recipe.getTitle(), recipeIngredients.size());

                    // Считаем количество совпадений между ингредиентами рецепта и пользовательскими
                    long matchCount = recipeIngredients.stream()
                            .filter(normalized::contains)
                            .count();

                    log.debug("Рецепт {}: совпадений с ингредиентами пользователя: {}", recipe.getTitle(), matchCount);

                    // Рассчитываем рейтинг (score) рецепта
                    double score = (double) matchCount / recipeIngredients.size();

                    log.debug("Рецепт {}: рейтинг (score) = {}", recipe.getTitle(), score);

                    return new RecipeMatchDto(RecipeDto.fromEntity(recipe), score);
                })
                .sorted(Comparator.comparingDouble(RecipeMatchDto::score).reversed()) // Сортируем по убыванию рейтинга
                .toList();

        log.debug("Поиск завершен. Найдено {} совпадений.", matches.size());

        return matches;
    }
}
