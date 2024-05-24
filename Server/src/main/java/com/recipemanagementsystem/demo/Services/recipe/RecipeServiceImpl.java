package com.recipemanagementsystem.demo.Services.recipe;

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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService{

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
                    ingredient = ingredientRepository.findByIngredientName(ingredientDTO.getIngredientName()).orElse(null);
                    if(ingredient == null){
                        ingredient = new Ingredient();
                        ingredient.setIngredientName(ingredientDTO.getIngredientName());
                        ingredient = ingredientRepository.save(ingredient);
                    }
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
        try {
            Optional<Recipe> optionalRecipe = recipeRepository.findRecipeByRecipeId(recipeId);

            if (optionalRecipe.isPresent()) {
                Recipe recipe = optionalRecipe.get();
                List<RecipeIngredient> existingRecipeIngredients = recipe.getRecipeIngredientsList();
                List<RecipeIngredient> updatedRecipeIngredients = new ArrayList<>();

                // Iterate through the provided list of DTOs
                for (RecipeIngredientDTO ingredientDTO : recipeIngredientDTOList) {
                    // Check if the ingredient exists in the repository by name
                    Optional<Ingredient> optionalIngredient = ingredientRepository.findByIngredientName(ingredientDTO.getIngredientName());

                    if (optionalIngredient.isPresent()) {
                        // If the ingredient exists, update its quantity and unit
                        Ingredient existingIngredient = optionalIngredient.get();

                        // Find the corresponding recipe ingredient
                        Optional<RecipeIngredient> existingRecipeIngredientOptional = existingRecipeIngredients.stream()
                                .filter(recipeIngredient -> recipeIngredient.getIngredient().equals(existingIngredient))
                                .findFirst();

                        if (existingRecipeIngredientOptional.isPresent()) {
                            RecipeIngredient recipeIngredient = existingRecipeIngredientOptional.get();
                            recipeIngredient.setQuantity(ingredientDTO.getQuantity());
                            recipeIngredient.setUnit(ingredientDTO.getUnit());

                            // Ensure the recipeIngredient has the recipe reference set
                            recipeIngredient.setRecipe(recipe);
                            updatedRecipeIngredients.add(recipeIngredient);
                        }
                    } else {
                        // If the ingredient does not exist, create a new one
                        Ingredient newIngredient = new Ingredient();
                        newIngredient.setIngredientName(ingredientDTO.getIngredientName());
                        newIngredient = ingredientRepository.save(newIngredient);

                        RecipeIngredient recipeIngredient = new RecipeIngredient();
                        recipeIngredient.setIngredient(newIngredient);
                        recipeIngredient.setQuantity(ingredientDTO.getQuantity());
                        recipeIngredient.setUnit(ingredientDTO.getUnit());

                        // Ensure the recipeIngredient has the recipe reference set
                        recipeIngredient.setRecipe(recipe);
                        updatedRecipeIngredients.add(recipeIngredient);
                    }
                }

                // Set the updated list of recipe ingredients for the recipe
                recipe.setRecipeIngredientsList(updatedRecipeIngredients);
                recipeRepository.save(recipe);
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<RecipeDTO> getAllRecipes() {
        return recipeRepository.findAll().stream().map(Recipe::getRecipeDTO).collect(Collectors.toList());
    }

    public boolean deleteRecipe(Long recipeId){
        if(recipeId != null && recipeRepository.findById(recipeId) != null){
            recipeRepository.deleteById(recipeId);
            return true;
        }
        return false;
    }

}
