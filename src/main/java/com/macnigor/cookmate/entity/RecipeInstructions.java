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

@Table("recipe_instructions")
public record RecipeInstructions(
        @Id
        @Column("id") // Защита от ошибки "I" not found
        Long id,

        @Column("instruction")
        String instruction,

        @Column("instruction_order") // Связываем поле 'i' с колонкой в БД
        int i
) {}