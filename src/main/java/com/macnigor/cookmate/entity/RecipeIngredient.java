/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table("recipe_ingredients")
public record RecipeIngredient(
        @Id
        @Column("id") // Защита от ошибки "I" not found
        Long id,

        @Column("ingredient_id")
        Long ingredientId,

        @Column("recipe_id") // Позволяет вашему методу init() видеть связь
        Long recipeId,

        @Column("amount")
        String amount
) {}





