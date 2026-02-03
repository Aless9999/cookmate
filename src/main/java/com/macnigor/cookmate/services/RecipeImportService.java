/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macnigor.cookmate.dto.RecipeJsonDto;
import com.macnigor.cookmate.entity.Ingredient;
import com.macnigor.cookmate.entity.Recipe;
import com.macnigor.cookmate.entity.RecipeIngredient;
import com.macnigor.cookmate.entity.RecipeInstructions;
import com.macnigor.cookmate.repositories.IngredientRepository;
import com.macnigor.cookmate.repositories.RecipeIngredientRepository;
import com.macnigor.cookmate.repositories.RecipeInstructionRepository;
import com.macnigor.cookmate.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecipeImportService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RecipeImportService.class);  // Логгер

    private final ObjectMapper objectMapper;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeInstructionRepository recipeInstructionRepository;

    public void importRecipesFromJson() {
        try {
            logger.info("Начинаю импорт рецептов из JSON файла...");

            InputStream inputStream = getClass().getResourceAsStream("/recipes.json");
            List<RecipeJsonDto> recipeDtos = objectMapper.readValue(inputStream, new TypeReference<>() {});

            for (RecipeJsonDto dto : recipeDtos) {
                logger.debug("Обрабатываю рецепт: {}", dto.title());

                // 1️⃣ Создаём список ингредиентов с проверкой на существование
                Set<RecipeIngredient> recipeIngredients = dto.ingredients().stream()
                        .map(ingredientDto -> {
                            Ingredient ingredient = ingredientRepository.findByName(ingredientDto.name())
                                    .orElseGet(() -> {
                                        Ingredient newIng = new Ingredient(null, ingredientDto.name());
                                        return ingredientRepository.save(newIng);
                                    });

                            return new RecipeIngredient(
                                    null,
                                    ingredient.id(),
                                    ingredientDto.amount()
                            );
                        })
                        .collect(Collectors.toSet());

                logger.debug("Созданы ингредиенты для рецепта: {}", dto.title());

                // 2️⃣ Создаём список инструкций
                List<RecipeInstructions> instructions = dto.instructions().stream()
                        .map(i -> new RecipeInstructions(null, i))
                        .toList();

                logger.debug("Созданы инструкции для рецепта: {}", dto.title());

                // 3️⃣ Создаём root Recipe с инструкциями и ингредиентами сразу
                Recipe recipe = new Recipe(
                        null,                    // ID сгенерируется автоматически
                        dto.title(),
                        dto.description(),
                        dto.imageUrl(),
                        instructions,
                        recipeIngredients
                );

                // 4️⃣ Сохраняем рецепт — JDBC автоматически подставит recipe_id для инструкций и ингредиентов
                Recipe savedRecipe = recipeRepository.save(recipe);

                logger.debug("Рецепт '{}' успешно сохранен с ID: {}", dto.title(), savedRecipe.id());
            }

            logger.info("Импорт рецептов завершен.");

        } catch (Exception e) {
            logger.error("Ошибка при импорте рецептов из JSON", e);
            throw new RuntimeException("Error importing recipes from JSON", e);
        }}

    @Override
    public void run(String... args) throws Exception {
        importRecipesFromJson();
    }
}

