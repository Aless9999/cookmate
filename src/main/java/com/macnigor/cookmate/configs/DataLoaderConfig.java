/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.configs;

import com.macnigor.cookmate.services.RecipeImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataLoaderConfig {

    private final RecipeImportService recipeImportService;
    private boolean isDataLoaded = false;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            if (!isDataLoaded) {
                recipeImportService.importRecipesFromJson();
                isDataLoaded = true;  // Помечаем, что данные уже загружены
            }
        };
    }
}
