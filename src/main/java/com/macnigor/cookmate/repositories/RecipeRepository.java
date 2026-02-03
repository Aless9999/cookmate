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

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    @Query("""
        SELECT DISTINCT r.* FROM recipes r
        JOIN recipe_ingredients ri ON r.id = ri.recipe_id
        JOIN ingredients i ON ri.ingredient_id = i.id
        WHERE LOWER(i.name) IN (:ingredients)
        ORDER BY r.title ASC
    """)
    List<Recipe> findRecipesWithIngredients(@Param("ingredients") List<String> ingredients);
}
