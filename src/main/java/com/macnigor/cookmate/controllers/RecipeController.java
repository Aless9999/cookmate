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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/search")
    public ResponseEntity <List<RecipeMessageDto>> search(@RequestBody List<String> ingredients) {
        // 1. Ищем рецепты по ингредиентам
        List<RecipeMatchDto> matches = recipeService.searchByIngredients(ingredients);

        // 2. Если рецептов нет
        if (matches.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        // 3. Берем топ-3 рецепта и формируем DTO
        List<RecipeMessageDto> top3Dtos = matches.stream()
                .limit(5)
                .map(m ->recipeMessageService.createRecipeMessage(m.recipe().toEntity()))
                .toList();

        // 4. Возвращаем список DTO
        return ResponseEntity.ok(top3Dtos);
    }

}
