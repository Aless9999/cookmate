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
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table("recipe_ingredients")
public record RecipeIngredient(
        @Id
        @Column("id")
        Long id,

        @Column("ingredient_id")
        Long ingredientId,

        @ReadOnlyProperty
        @Column("recipe_id")
        Long recipeId,

        @Column("amount")
        String amount
) {
    public RecipeIngredient(Long id, Long ingredientId, String amount) {
        this(id, ingredientId, 0L, amount);
    }
}





