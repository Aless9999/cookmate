package com.macnigor.cookmate;

import com.macnigor.cookmate.dto.*;
import com.macnigor.cookmate.entity.*;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@RegisterReflectionForBinding(classes ={
        User.class,
        RecipeIngredient.class,
        Recipe.class,
        Ingredient.class,

AuthRequest.class,
AuthResponse.class,
IngredientDto.class,
RecipeDto.class,
RecipeJsonDto.class,
RecipeMatchDto.class,
RecipeMessageDto.class,
RegisterResponse.class,
UserRegisterDto.class})
@SpringBootApplication
public class CookMateApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookMateApplication.class, args);
	}

}
