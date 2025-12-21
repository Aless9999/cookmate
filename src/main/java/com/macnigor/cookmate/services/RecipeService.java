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
import com.macnigor.cookmate.entity.RecipeIngredient;
import com.macnigor.cookmate.mapper.RecipeMapper;
import com.macnigor.cookmate.repositories.RecipeRepository;
import com.macnigor.cookmate.utils.IngredientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;



@Slf4j
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;


    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    //Метод для получения рецептов из базы данных по ингридиентам пользователя
    public List<Recipe> getRecipeWithIngredientsUserChoice(List<String> userChoiceIngredients) {

        return recipeRepository.findRecipesWithIngredients(userChoiceIngredients);
    }





    // Метод для поиска рецептов по ингредиентам
    public List<RecipeMatchDto> searchByIngredients(List<String> userIngredients) {
        log.debug("Начало поиска рецептов по ингредиентам. Пользовательские ингредиенты: {}", userIngredients);

        // Нормализуем пользовательские ингредиенты (нижний регистр и убираем пробелы)
        List<String> normalized = userIngredients.stream()
                .map(String::toLowerCase)
                .map(String::trim)
                .toList();

        log.debug("Нормализованные ингредиенты пользователя: {}", normalized);

        // Формируем список совпадений
        List<RecipeMatchDto> matches = getRecipeWithIngredientsUserChoice(normalized).stream()
                .map(recipe -> {
                    // Получаем только совпадающие ингредиенты с пользовательскими
                    List<RecipeIngredient> matchedIngredients = recipe.getRecipeIngredients().stream()
                            .filter(ri -> normalized.contains(ri.getIngredient().getName().toLowerCase()))
                            .toList();

                    long score = matchedIngredients.size(); // количество совпадений

                    // Конвертируем количество совпавших ингредиентов в базовую единицу (г, мл и т.п.)
                    List<Integer> amounts = matchedIngredients.stream()
                            .map(ri -> IngredientUtils.parseAndConvertAmount(ri.getAmount()))
                            .toList();

                    return new RecipeMatchDto(recipeMapper.fromEntity(recipe), score, amounts);
                })
                // Сортировка
                .sorted(
                        Comparator.comparingLong(RecipeMatchDto::score).reversed() // сначала по количеству совпадений
                                .thenComparing(
                                        (r1, r2) -> {
                                            // вторичная сортировка по максимальной массе совпавших ингредиентов
                                            int max1 = r1.amount().stream().max(Integer::compareTo).orElse(0);
                                            int max2 = r2.amount().stream().max(Integer::compareTo).orElse(0);
                                            return Integer.compare(max2, max1); // большее количество вверх
                                        }
                                )
                )
                .toList();

        log.debug("Поиск завершен. Найдено {} совпадений.", matches.size());
        return matches;
    }



}
