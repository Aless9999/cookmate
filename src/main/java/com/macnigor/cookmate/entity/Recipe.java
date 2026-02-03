package com.macnigor.cookmate.entity;



import com.macnigor.cookmate.projection.RecipeView;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Set;

@Table("recipes")
public record Recipe(
        @Id Long id,
        String title,
        String description,
        String imageUrl,
        @MappedCollection(idColumn = "recipe_id", keyColumn = "instruction_order")
        List<RecipeInstructions> instructions,
        @MappedCollection(idColumn = "recipe_id")
        Set<RecipeIngredient> ingredients
) {}
