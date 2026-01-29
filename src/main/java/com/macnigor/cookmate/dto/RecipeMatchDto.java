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

import java.util.List;

public record RecipeMatchDto(
        RecipeDto recipe,
        long matchCount,
        double coveragePercent,
        List<Integer> amount
) {
}

