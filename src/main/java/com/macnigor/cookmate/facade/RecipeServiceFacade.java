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

import com.macnigor.cookmate.dto.RecipeJsonDto;
import com.macnigor.cookmate.services.RecipeCommandService;
import com.macnigor.cookmate.services.RecipeQueryService;
import com.macnigor.cookmate.dto.RecipeMessageDto;
import com.macnigor.cookmate.services.RecipeMessageService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeServiceFacade {
    private final RecipeCommandService recipeCommandService;
    private final RecipeQueryService recipeQueryService;
    private final RecipeMessageService recipeMessageService;

    public List<RecipeMessageDto> searchAndCreateStringMessage(@NotEmpty(
            message = "Список ингредиентов не может быть пустым") List<String> ingredients) {
        return recipeQueryService.searchByIngredients(ingredients).stream()
                .map(recipeDto -> recipeMessageService.createRecipeMessage(recipeDto.recipe()))
                .toList();
    }

    public void createNewRecipe(RecipeJsonDto recipe) {
        recipeCommandService.create(recipe);
    }
}

