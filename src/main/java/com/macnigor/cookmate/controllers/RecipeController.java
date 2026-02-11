/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 */

package com.macnigor.cookmate.controllers;

import com.macnigor.cookmate.dto.RecipeMessageDto;
import com.macnigor.cookmate.facade.RecipeServiceFacade;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@AllArgsConstructor
public class RecipeController {

    private final RecipeServiceFacade recipeServiceFacade;




    @PostMapping("/search")
    public ResponseEntity <List<RecipeMessageDto>> searchRecipes(@RequestBody @NotEmpty(
            message = "Список ингредиентов не может быть пустым")
             List<String> ingredients) {
        List<RecipeMessageDto> topRecipe = recipeServiceFacade.searchAndCreateStringMessage(ingredients);

        System.out.println(topRecipe);
         return topRecipe.isEmpty()?
                    ResponseEntity.noContent().build():
                    ResponseEntity.ok(topRecipe);
        }
    }


