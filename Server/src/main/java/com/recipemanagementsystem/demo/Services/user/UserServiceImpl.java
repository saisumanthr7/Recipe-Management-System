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
import java.util.Optional;

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

    @Override
    public boolean updateRecipe(Long recipeId, RecipeDTO recipeDTO) {
        try{
           Optional<Recipe> optionalRecipe = recipeRepository.findRecipeByRecipeId(recipeId);

//            System.out.println("Received recipeId: " + recipeId);
//            System.out.println("Received recipeDTO: " + recipeDTO);
           if(optionalRecipe.isPresent()){
               Recipe existingRecipe = optionalRecipe.get();
//               System.out.println("Found existing recipe: " + existingRecipe);
               if (recipeDTO.getImage() != null && !recipeDTO.getImage().isEmpty()) {
                   existingRecipe.setImage(recipeDTO.getImage().getBytes());
               }
               existingRecipe.setRecipeName(recipeDTO.getRecipeName());
               existingRecipe.setDescription(recipeDTO.getDescription());
               existingRecipe.setTotalTime(recipeDTO.getTotal_time());
               existingRecipe.setPrep_time(recipeDTO.getPrep_time());
               existingRecipe.setCook_time(recipeDTO.getCook_time());
               existingRecipe.setYield(recipeDTO.getYield());

//               System.out.println("Updated recipe: " + existingRecipe);
               recipeRepository.save(existingRecipe);
               return true;
           }
           else{
               System.out.println("Recipe not found for ID: " + recipeId);
               return false;
           }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean updateRecipeIngredients(Long recipeId, List<RecipeIngredientDTO> recipeIngredientDTOList) {
        try{
            Optional<Recipe> optionalRecipe = recipeRepository.findRecipeByRecipeId(recipeId);

            System.out.println("Received recipeId: " + recipeId);
            System.out.println("Received recipeDTO: " + recipeIngredientDTOList);

            if(optionalRecipe.isPresent()){
                Recipe recipe = optionalRecipe.get();
                List<RecipeIngredient> existingRecipeIngredients = recipe.getRecipeIngredientsList();

               System.out.println("Found existing recipe Ingredients: " + existingRecipeIngredients);
                List<RecipeIngredient> updatedRecipeIngredients = new ArrayList<>();

                // Iterate through the provided list of DTOs
                for(RecipeIngredientDTO ingredientDTO : recipeIngredientDTOList){
                    Ingredient ingredient = null;

                    //check if the ingredient Id is provided
                    if(ingredientDTO.getIngredientId() != null){
                        ingredient = ingredientRepository.findById(ingredientDTO.getIngredientId()).orElse(null);
                    }

                    //if the ingredient doesnot exist and the name is provided, create a new ingredient
                    if(ingredient == null && ingredientDTO.getIngredientName() != null){
                        ingredient = new Ingredient();
                        ingredient.setIngredientName(ingredientDTO.getIngredientName());
                        ingredient = ingredientRepository.save(ingredient);
                    }

                    // If no ingredient found or provided, skip to the next iteration
                    if(ingredient == null){
                        continue;
                    }

                    // Check if the ingredient exists in the existing recipe ingredients
                    Ingredient finalIngredient = ingredient;
                    Optional<RecipeIngredient> existingRecipeIngredientOptional = existingRecipeIngredients.stream()
                            .filter(recipeIngredient -> recipeIngredient.getIngredient().getIngredientId().equals(finalIngredient.getIngredientId()))
                            .findFirst();
                    RecipeIngredient recipeIngredient;
                    // If the ingredient exists, update its quantity and unit
                    if(existingRecipeIngredientOptional.isPresent()){
                        recipeIngredient = existingRecipeIngredientOptional.get();
                        recipeIngredient.setQuantity(ingredientDTO.getQuantity());
                        recipeIngredient.setUnit(ingredientDTO.getUnit());
                    }
                    else {
                        // If the ingredient is new, create a new recipe ingredient
                        recipeIngredient = new RecipeIngredient();
                        recipeIngredient.setIngredient(ingredient);
                        recipeIngredient.setQuantity(ingredientDTO.getQuantity());
                        recipeIngredient.setUnit(ingredientDTO.getUnit());
                    }
                    updatedRecipeIngredients.add(recipeIngredient);

               System.out.println("Updated recipe: " + updatedRecipeIngredients);
                }

                // Set the updated list of recipe ingredients for the recipe

                recipe.setRecipeIngredientsList(updatedRecipeIngredients);

                return true;
            }else{
                return false;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
