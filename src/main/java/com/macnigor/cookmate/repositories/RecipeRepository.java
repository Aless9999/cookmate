/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.repositories;

import com.macnigor.cookmate.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public interface RecipeRepository extends JpaRepository<Recipe,Long> {

    @Query("SELECT DISTINCT r FROM Recipe r " +
            "JOIN r.recipeIngredients ri " +
            "JOIN ri.ingredient i " +
            "WHERE LOWER(i.name) IN :ingredients " +
            "ORDER BY r.title ASC")
    List<Recipe> findRecipesWithIngredients(@Param("ingredients") List<String> ingredients);

}
