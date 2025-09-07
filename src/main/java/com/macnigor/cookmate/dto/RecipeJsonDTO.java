/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class RecipeJsonDTO {
    private String title;
    private String description;
    private List<String> instructions; // список шагов
    private String imageUrl;
    private List<IngredientDTO> ingredients;


}
