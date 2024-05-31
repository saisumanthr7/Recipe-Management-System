package com.recipemanagementsystem.demo.Services.recipe;

import com.recipemanagementsystem.demo.Dto.Recipe.RecipeCategoryDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeIngredientDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeInstructionsDTO;

import java.io.IOException;
import java.util.List;

public interface RecipeService {


    boolean createRecipe(Long userId, RecipeDTO recipeDTO) throws IOException;

    boolean updateRecipe(Long recipeId, RecipeDTO recipeDTO);


    boolean updateRecipeCategories(Long recipeId, List<RecipeCategoryDTO> recipeCategoryDTOList);

    boolean updateRecipeIngredients(Long recipeId, List<RecipeIngredientDTO> recipeIngredientDTOList);

    boolean updateRecipeInstructions(Long recipeId, List<RecipeInstructionsDTO> recipeInstructionsDTO);

    List<RecipeDTO> getAllRecipes();

    List<RecipeIngredientDTO> getAllRecipeIngredients();

    List<RecipeInstructionsDTO> getAllRecipeInstructions();

    boolean deleteRecipe(Long recipeId);

    boolean deleteRecipeIngredients(Long recipeId,Long recipeIngredientId);

    boolean deleteRecipeInstructions(Long recipeId, Long recipeInstructionId);

}
