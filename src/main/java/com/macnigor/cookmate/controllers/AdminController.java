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

import com.macnigor.cookmate.dto.RecipeJsonDto;
import com.macnigor.cookmate.facade.RecipeServiceFacade;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/recipe/admin")
public class AdminController {

private final RecipeServiceFacade recipeServiceFacade;

    @PostMapping("/recipe")
    @Operation(summary = "Добавить рецепт через форму")
    public String addNewRecipe(@RequestBody RecipeJsonDto recipe){
        recipeServiceFacade.createNewRecipe(recipe);
        return "Создан новый рецепт";
    }
}
