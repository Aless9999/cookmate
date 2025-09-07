/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.controllers;

import com.macnigor.cookmate.dto.ApiResponse;
import com.macnigor.cookmate.dto.RecipeMatchDto;
import com.macnigor.cookmate.services.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<RecipeMatchDto>>> search(
            @RequestParam List<String> ingredients
    ) {
        List<RecipeMatchDto> results = recipeService.searchByIngredients(ingredients);
        return ResponseEntity.ok(new ApiResponse<>(
                "Recipes found: " + results.size(),
                200,
                results
        ));
    }
}
