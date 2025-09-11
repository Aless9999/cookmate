/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 */

package com.macnigor.cookmate.controllers;

import com.macnigor.cookmate.dto.RecipeMatchDto;
import com.macnigor.cookmate.services.RecipeMessageService;
import com.macnigor.cookmate.services.RecipeService;
import com.macnigor.cookmate.dto.RecipeMessageDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeMessageService recipeMessageService;

    public RecipeController(RecipeService recipeService,
                            RecipeMessageService recipeMessageService) {
        this.recipeService = recipeService;
        this.recipeMessageService = recipeMessageService;
    }

    @GetMapping("/search")
    public RecipeMessageDto search(@RequestParam List<String> ingredients) {
        // Ищем рецепты по ингредиентам
        List<RecipeMatchDto> matches = recipeService.searchByIngredients(ingredients);

        if (matches.isEmpty()) {
            return new RecipeMessageDto("❌ Рецепты не найдены по указанным ингредиентам.", null);
        }

        // Берем лучший (первый) рецепт
        RecipeMatchDto bestMatch = matches.get(0);

        // Создаем сообщение для пользователя
        return recipeMessageService.createRecipeMessage(bestMatch.recipe().toEntity());
    }
}
