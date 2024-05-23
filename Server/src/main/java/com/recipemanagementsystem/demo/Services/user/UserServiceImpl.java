package com.recipemanagementsystem.demo.Services.user;

import com.recipemanagementsystem.demo.Dto.RecipeDTO;
import com.recipemanagementsystem.demo.Entity.Recipe;
import com.recipemanagementsystem.demo.Repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RecipeRepository recipeRepository;


    @Override
    public boolean createRecipe(RecipeDTO recipeDTO) throws IOException {
        try{
            Recipe recipe = new Recipe();
            recipe.setRecipeName(recipeDTO.getRecipeName());
            System.out.println(recipeDTO.getRecipeName() + recipeDTO.getDescription());
            recipe.setDescription(recipeDTO.getDescription());
            recipe.setTotalTime(recipeDTO.getTotal_time());
            recipe.setPrep_time(recipeDTO.getPrep_time());
            recipe.setCook_time(recipeDTO.getCook_time());
            recipe.setYield(recipeDTO.getYield());

            if (recipeDTO.getImage() != null && !recipeDTO.getImage().isEmpty()) {
                recipe.setImage(recipeDTO.getImage().getBytes());
            } else {
                recipe.setImage(null);
            }
            recipeRepository.save(recipe);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
