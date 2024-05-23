package com.recipemanagementsystem.demo.Services.user;

import com.recipemanagementsystem.demo.Dto.Recipe.RecipeDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeIngredientDTO;
import com.recipemanagementsystem.demo.Entity.Ingredient;
import com.recipemanagementsystem.demo.Entity.Recipe;
import com.recipemanagementsystem.demo.Entity.RecipeIngredient;
import com.recipemanagementsystem.demo.Repository.IngredientRepository;
import com.recipemanagementsystem.demo.Repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;


    @Override
    public boolean createRecipe(RecipeDTO recipeDTO) throws IOException {
        try{
            Recipe recipe = new Recipe();
            recipe.setRecipeName(recipeDTO.getRecipeName());
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

            List<RecipeIngredient> recipeIngredients = new ArrayList<>();
            for(RecipeIngredientDTO ingredientDTO : recipeDTO.getRecipeIngredientDTOList()){

                Ingredient ingredient = null;
                if(ingredientDTO.getIngredientId() != null){
                    ingredient = ingredientRepository.findById(ingredientDTO.getIngredientId())
                            .orElse(null);
                }

                if(ingredient == null && ingredientDTO.getIngredientName() != null){
                    ingredient = new Ingredient();
                    ingredient.setIngredientName(ingredientDTO.getIngredientName());
                    ingredient = ingredientRepository.save(ingredient);
                }

                if(ingredient == null){
                    continue;
                }
                RecipeIngredient recipeIngredient = new RecipeIngredient();
                recipeIngredient.setRecipe(recipe);
                recipeIngredient.setIngredient(ingredient);
                recipeIngredient.setQuantity(ingredientDTO.getQuantity());
                recipeIngredient.setUnit(ingredientDTO.getUnit());
                recipeIngredients.add(recipeIngredient);
            }
            recipe.setRecipeIngredientsList(recipeIngredients);
            recipeRepository.save(recipe);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
