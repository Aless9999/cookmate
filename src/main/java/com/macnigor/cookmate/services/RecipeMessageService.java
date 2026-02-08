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


    import com.macnigor.cookmate.projection.RecipeView;
    import com.macnigor.cookmate.dto.RecipeMessageDto;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
//сервис для формирования сообщения
    @Service
    public class RecipeMessageService {

        @Value("${base.url}")
        private String baseUrl; // Базовый URL для изображений

        public RecipeMessageDto createRecipeMessage(RecipeView recipe) {
            // Формируем строку рецепта
            StringBuilder message = new StringBuilder();

            // Название рецепта с эмодзи
            message.append("🍽 **").append(recipe.getTitle()).append("**\n\n");

            // Описание рецепта
            if (recipe.getDescription() != null && !recipe.getDescription().isEmpty()) {
                message.append("📝 *").append(recipe.getDescription()).append("*\n\n");
            }

            // Ингредиенты
            if (recipe.getIngredientsList() != null && !recipe.getIngredientsList().isEmpty()) {
                message.append("🔑 **Ингредиенты:**\n");
                for (String recipeIngredient : recipe.getIngredientsList()) {

                    message.append(" - ").append(recipeIngredient).append("\n");
                }
            } else {
                message.append("🔑 **Ингредиенты не указаны**\n");
            }

            // Инструкция
            if (recipe.getInstructions() != null && !recipe.getInstructions().isEmpty()) {
                message.append("\n🚀 *Инструкция:*\n");
                for (String step : recipe.getInstructions()) {
                    message.append(" - ").append(step).append("\n");
                }
            } else {
                message.append("\n🚀 *Инструкция не указана*\n");
            }

            // Формируем корректный URL изображения
            String imageUrl = null;
            System.out.println("DEBUG: Raw URL from DB: '" + recipe.getImageUrl() + "'");
            if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
                // Получаем только имя файла из пути (кроссплатформенно)
                String fileName = new java.io.File(recipe.getImageUrl()).getName();
                imageUrl = baseUrl + fileName;

            } else {
                message.append("\n🖼️ *Изображение не доступно*\n");
            }

            // Возвращаем DTO
            return new RecipeMessageDto(message.toString(), imageUrl);
        }
    }


