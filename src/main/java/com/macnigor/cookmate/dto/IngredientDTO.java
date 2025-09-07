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
@Setter
@Getter
public class IngredientDTO {

        private String name;
        private String amount; // здесь "300 г", "2 ст. л.", "по вкусу"
    }

