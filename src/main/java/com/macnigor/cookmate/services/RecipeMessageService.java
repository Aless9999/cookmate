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

// RecipeMessageService.java — сервис для формирования сообщения
import com.macnigor.cookmate.dto.RecipeMessageDto;
import com.macnigor.cookmate.entity.Ingredient;
import com.macnigor.cookmate.entity.Recipe;
import com.macnigor.cookmate.entity.RecipeIngredient;
import org.springframework.stereotype.Service;

@Service
public class RecipeMessageService {

    public RecipeMessageDto createRecipeMessage(Recipe recipe) {
        // Формируем строку рецепта
        StringBuilder message = new StringBuilder();

        // Название рецепта с эмодзи
        message.append("🍽 **" + recipe.getTitle() + "**\n\n");

        // Описание рецепта
        if (recipe.getDescription() != null && !recipe.getDescription().isEmpty()) {
            message.append("📝 *" + recipe.getDescription() + "*\n\n");
        }

        // Ингредиенты: проверяем, что список не пуст
        if (recipe.getRecipeIngredients() != null && !recipe.getRecipeIngredients().isEmpty()) {
            message.append("🔑 **Ингредиенты:**\n");
            for (RecipeIngredient recipeIngredient : recipe.getRecipeIngredients()) {
                Ingredient ingredient = recipeIngredient.getIngredient();  // Получаем ингредиент
                message.append(" - " + recipeIngredient.getAmount() + " " + ingredient.getName() + "\n");
            }
        } else {
            message.append("🔑 **Ингредиенты не указаны**\n");
        }

        // Инструкция: проверяем, что шаги не пустые
        if (recipe.getInstructions() != null && !recipe.getInstructions().isEmpty()) {
            message.append("\n🚀 *Инструкция:*\n");
            for (String step : recipe.getInstructions()) {
                message.append(" - " + step + "\n");
            }
        } else {
            message.append("\n🚀 *Инструкция не указана*\n");
        }

        // Добавляем ссылку на изображение, если оно есть
        if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
            message.append("\n🖼️ *Посмотреть изображение:* " + recipe.getImageUrl() + "\n");
        } else {
            message.append("\n🖼️ *Изображение не доступно*\n");
        }

        // Формируем DTO и возвращаем
        return new RecipeMessageDto(message.toString(), recipe.getImageUrl());
    }

}

