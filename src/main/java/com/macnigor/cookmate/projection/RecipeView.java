/*
 *
 *  * Copyright 2025 Кодер Als
 *  *
 *  * Licensed under the Apache License, Version 2.0
 *  * See https://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.macnigor.cookmate.projection;

import java.util.List;

public interface RecipeView {
    Long getId();
    String getTitle();
    String getDescription();
    List<String> getInstructions();
    String getImageUrl();
    List<String> getIngredientsList();

}
