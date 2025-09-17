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

import com.macnigor.cookmate.services.AppStateService;
import com.macnigor.cookmate.services.RecipeImportService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@Getter
@Slf4j // Ломбок генерирует логгер для данного класса
public class DataLoaderConfig {

    private final RecipeImportService recipeImportService;
    private final AppStateService appStateService;

    public DataLoaderConfig(RecipeImportService recipeImportService, AppStateService appStateService) {
        this.recipeImportService = recipeImportService;
        this.appStateService = appStateService;
    }

    @Bean
    public CommandLineRunner loadData() {
        boolean isDataLoaded = appStateService.getState();
        return args -> {
            log.info("Проверка состояния загрузки данных. Загрузки данных: {}", isDataLoaded);

            if (!isDataLoaded) {
                log.info("Данные не загружены. Запуск импорта рецептов...");
                try {
                    recipeImportService.importRecipesFromJson();
                    appStateService.changedState(true);  // Помечаем, что данные уже загружены
                    log.info("Данные успешно загружены.");
                } catch (Exception e) {
                    log.error("Ошибка при загрузке данных: {}", e.getMessage(), e);
                }
            } else {
                log.info("Данные уже загружены, повторная загрузка не требуется.");
            }
        };
    }
}
