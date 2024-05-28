package com.recipemanagementsystem.demo.Services.recipe;

import com.recipemanagementsystem.demo.Dto.Recipe.RecipeDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeIngredientDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeInstructionsDTO;
import com.recipemanagementsystem.demo.Entity.Ingredient;
import com.recipemanagementsystem.demo.Entity.Recipe;
import com.recipemanagementsystem.demo.Entity.RecipeIngredient;
import com.recipemanagementsystem.demo.Entity.RecipeInstructions;
import com.recipemanagementsystem.demo.Repository.recipe.IngredientRepository;
import com.recipemanagementsystem.demo.Repository.recipe.RecipeIngredientRepository;
import com.recipemanagementsystem.demo.Repository.recipe.RecipeInstructionsRepository;
import com.recipemanagementsystem.demo.Repository.recipe.RecipeRepository;
import jakarta.transaction.Transactional;
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
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeInstructionsRepository recipeInstructionsRepository;

    @Override
    @Transactional
    public boolean createRecipe(RecipeDTO recipeDTO) throws IOException {
        try{
            Recipe recipe = new Recipe();
            recipe.setRecipeName(recipeDTO.getRecipeName());
            recipe.setDescription(recipeDTO.getDescription());
            recipe.setTotalTime(recipeDTO.getTotal_time());
            recipe.setPrepTime(recipeDTO.getPrep_time());
            recipe.setCookTime(recipeDTO.getCook_time());
            recipe.setYield(recipeDTO.getYield());

            if (recipeDTO.getImage() != null && !recipeDTO.getImage().isEmpty()) {
                recipe.setRecipeImage(recipeDTO.getImage().getBytes());
            } else {
                recipe.setRecipeImage(null);
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

            List<RecipeInstructions> recipeInstructions = new ArrayList<>();
            for(RecipeInstructionsDTO recipeInstructionsDTO: recipeDTO.getRecipeInstructionsDTOList()){
                RecipeInstructions recipeInstructions1 = new RecipeInstructions();
                recipeInstructions1.setRecipe(recipe);
                recipeInstructions1.setStepNumber(recipeInstructionsDTO.getStepNumber());
                recipeInstructions1.setInstruction(recipeInstructionsDTO.getInstruction());
                if(recipeInstructionsDTO.getInstructionImage() !=null && !recipeInstructionsDTO.getInstructionImage().isEmpty()){
                    recipeInstructions1.setInstructionImage(recipeInstructionsDTO.getInstructionImage().getBytes());
                }else {
                    recipeInstructions1.setInstructionImage(null);
                }
                recipeInstructions.add(recipeInstructions1);
            }
            recipe.setRecipeInstructionsList(recipeInstructions);
            recipe.setRecipeIngredientsList(recipeIngredients);
            recipeRepository.save(recipe);
            return true;
        }catch (IOException e) {
            // Handle specific IOException
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // Handle generic exception
            e.printStackTrace();
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
                    existingRecipe.setRecipeImage(recipeDTO.getImage().getBytes());
                }
                existingRecipe.setRecipeName(recipeDTO.getRecipeName());
                existingRecipe.setDescription(recipeDTO.getDescription());
                existingRecipe.setTotalTime(recipeDTO.getTotal_time());
                existingRecipe.setPrepTime(recipeDTO.getPrep_time());
                existingRecipe.setCookTime(recipeDTO.getCook_time());
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
    public boolean updateRecipeInstructions(Long recipeId, List<RecipeInstructionsDTO> recipeInstructionsDTOList){
        try{
            Optional<Recipe> optionalRecipe = recipeRepository.findRecipeByRecipeId(recipeId);

            if(optionalRecipe.isPresent()){
                Recipe recipe = optionalRecipe.get();
                List<RecipeInstructions> existingRecipeInstructions = recipe.getRecipeInstructionsList();
                List<RecipeInstructions> updatedRecipeInstructions = new ArrayList<>();

                for(RecipeInstructionsDTO recipeInstructionsDTO: recipeInstructionsDTOList){
                    RecipeInstructions existingInstruction = existingRecipeInstructions.stream()
                            .filter(recipeInstructions -> recipeInstructions.getStepNumber() == recipeInstructionsDTO.getStepNumber())
                            .findFirst().orElse(null);
                    if(existingInstruction != null){
                        existingInstruction.setInstruction(recipeInstructionsDTO.getInstruction());
                        if (recipeInstructionsDTO.getInstructionImage() != null && !recipeInstructionsDTO.getInstructionImage().isEmpty()) {
                            existingInstruction.setInstructionImage(recipeInstructionsDTO.getInstructionImage().getBytes());
                        }
                        updatedRecipeInstructions.add(existingInstruction);
                    }else{
                        RecipeInstructions newInstruction = new RecipeInstructions();
                        newInstruction.setRecipe(recipe);
                        newInstruction.setStepNumber(recipeInstructionsDTO.getStepNumber());
                        newInstruction.setInstruction(recipeInstructionsDTO.getInstruction());

                        if (recipeInstructionsDTO.getInstructionImage() != null && !recipeInstructionsDTO.getInstructionImage().isEmpty()) {
                            newInstruction.setInstructionImage(recipeInstructionsDTO.getInstructionImage().getBytes());
                        }
                        newInstruction.setRecipe(recipe);
                        updatedRecipeInstructions.add(newInstruction);
                    }
                }
                recipe.setRecipeInstructionsList(updatedRecipeInstructions);
                recipeRepository.save(recipe);
                return true;
            }else{
                System.out.println("Recipe not found for ID: " + recipeId);
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public List<RecipeDTO> getAllRecipes() {
        return recipeRepository.findAll().stream().map(Recipe::getRecipeDTO).collect(Collectors.toList());
    }

    @Override
    public List<RecipeIngredientDTO> getAllRecipeIngredients() {
        return recipeIngredientRepository.findAll().stream().map(RecipeIngredient::getRecipeIngredientDTO).collect(Collectors.toList());
    }

    @Override
    public List<RecipeInstructionsDTO> getAllRecipeInstructions() {
        return recipeInstructionsRepository.findAll().stream().map(RecipeInstructions::getRecipeInstructionsDTO).collect(Collectors.toList());
    }

    public boolean deleteRecipe(Long recipeId) {
        if (recipeId == null) {
            return false; // Recipe ID is null, nothing to delete
        }

        try {
            Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
            if (optionalRecipe.isPresent()) {
                recipeRepository.deleteById(recipeId);
                return true;
            } else {
                System.out.println("Recipe not found for ID: " + recipeId);
                return false;
            }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return false;
        }
        return false;
    }


}
