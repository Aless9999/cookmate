package com.macnigor.cookmate.entity;

import com.macnigor.cookmate.projection.RecipeView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "recipes")
@Getter
@Setter
public class Recipe implements RecipeView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @ElementCollection
    @CollectionTable(name = "recipe_instructions", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "instruction")
    private List<String> instructions;
    private String imageUrl;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeIngredients;

    @Override
    public List<String> getIngredientsList() {
        if(recipeIngredients==null)return List.of();
        return recipeIngredients.stream()
                .map(r->r.getIngredient().getName()+ ((r.getAmount()!=null)? " ("+r.getAmount()+")":""))
                .toList();
    }
}
