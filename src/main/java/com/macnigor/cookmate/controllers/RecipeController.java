/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 */

package com.macnigor.cookmate.controllers;

import com.macnigor.cookmate.facade.RecipeSearchFacade;
import com.macnigor.cookmate.dto.RecipeMessageDto;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeSearchFacade searchFacade;

    public RecipeController(RecipeSearchFacade searchFacade) {
        this.searchFacade = searchFacade;
    }


    @PostMapping("/search")
    public ResponseEntity <List<RecipeMessageDto>> searchRecipes(@RequestBody @NotEmpty(
            message = "Список ингредиентов не может быть пустым")
             List<String> ingredients) {
        List<RecipeMessageDto> topRecipe = searchFacade.searchAndCreateStringMessage(ingredients);

        System.out.println(topRecipe);
         return topRecipe.isEmpty()?
                    ResponseEntity.noContent().build():
                    ResponseEntity.ok(topRecipe);
        }
    }


