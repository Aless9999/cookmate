/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.indexes;

import com.macnigor.cookmate.entity.Ingredient;
import com.macnigor.cookmate.entity.RecipeIngredient;
import com.macnigor.cookmate.repositories.IngredientRepository;
import com.macnigor.cookmate.repositories.RecipeIngredientRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class RecipeSearchIndex {

    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    // === Индексы ===

    // "лук" -> 1
    private Map<String, Long> ingredientNameToId = new HashMap<>();

    // ingredientId -> recipeIds
    private final Map<Long, Set<Long>> ingredientToRecipeIds = new HashMap<>();

    // recipeId -> ingredientIds
    private final Map<Long, Set<Long>> recipeToIngredientIds = new HashMap<>();

    @Getter
    private  Map<Long, Ingredient> allIngredientsById = new HashMap<>();


    public RecipeSearchIndex(
            IngredientRepository ingredientRepository,
            RecipeIngredientRepository recipeIngredientRepository
    ) {
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    /**
     * Строим индекс при старте приложения
     */
    @PostConstruct
    void init() {
        // 1. Ингредиенты
        List<Ingredient> ingredients = StreamSupport.stream(
                ingredientRepository.findAll().spliterator(), false
        ).toList();

        ingredientNameToId = ingredients.stream()
                .collect(Collectors.toMap(
                        ing -> ing.name().toLowerCase(),
                        Ingredient::id,
                        (a, b) -> a
                ));

        // 2. Связи рецепт–ингредиент
        List<RecipeIngredient> recipeIngredients = StreamSupport.stream(
                recipeIngredientRepository.findAll().spliterator(), false
        ).toList();

        for (RecipeIngredient ri : recipeIngredients) {
            ingredientToRecipeIds
                    .computeIfAbsent(ri.ingredientId(), k -> new HashSet<>())
                    .add(ri.recipeId());

            recipeToIngredientIds
                    .computeIfAbsent(ri.recipeId(), k -> new HashSet<>())
                    .add(ri.ingredientId());
        }

        allIngredientsById = ingredients.stream()
                .collect(Collectors.toMap(
                        Ingredient::id,
                        ing->ing,
                        (a,b)->a
                ));
    }

    // ===================== GETTERS =====================

    public Long getIngredientIdByName(String name) {
        return name == null ? null : ingredientNameToId.get(name.toLowerCase());
    }

    public Set<Long> getRecipeIdsByIngredientId(Long ingredientId) {
        return ingredientToRecipeIds.getOrDefault(ingredientId, Set.of());
    }

    public Set<Long> getIngredientIdsByRecipeId(Long recipeId) {
        return recipeToIngredientIds.getOrDefault(recipeId, Set.of());
    }
}