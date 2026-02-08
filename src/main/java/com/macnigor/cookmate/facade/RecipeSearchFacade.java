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

import com.macnigor.cookmate.dto.RecipeMessageDto;
import com.macnigor.cookmate.services.RecipeMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeSearchFacade {
    private final RecipeMessageService recipeMessageService;
    private final RecipeServiceFacade recipeService;

    public RecipeSearchFacade(RecipeMessageService recipeMessageService, RecipeServiceFacade recipeService) {
        this.recipeMessageService = recipeMessageService;
        this.recipeService = recipeService;
    }


    public List<RecipeMessageDto> searchAndCreateStringMessage(List<String>ingredients){
        return recipeService.searchByIngredients(ingredients)
        .stream()
        .map(recipeDto -> recipeMessageService.createRecipeMessage(recipeDto.recipe()))
                .toList();

    }

}
