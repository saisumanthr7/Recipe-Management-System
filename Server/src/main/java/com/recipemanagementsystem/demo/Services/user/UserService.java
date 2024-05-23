package com.recipemanagementsystem.demo.Services.user;

import com.recipemanagementsystem.demo.Dto.RecipeDTO;

import java.io.IOException;

public interface UserService {

    boolean createRecipe(RecipeDTO recipeDTO) throws IOException;
}
