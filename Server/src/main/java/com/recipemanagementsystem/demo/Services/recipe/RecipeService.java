package com.recipemanagementsystem.demo.Services.recipe;

import com.recipemanagementsystem.demo.Dto.Recipe.RecipeDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeIngredientDTO;

import java.io.IOException;
import java.util.List;

public interface RecipeService {


    boolean createRecipe(RecipeDTO recipeDTO) throws IOException;
    boolean updateRecipe(Long recipeId, RecipeDTO recipeDTO);
    boolean updateRecipeIngredients(Long recipeId, List<RecipeIngredientDTO> recipeIngredientDTOList);

    List<RecipeDTO> getAllRecipes();
}
