package com.recipemanagementsystem.demo.Services.recipe;

import com.recipemanagementsystem.demo.Dto.Recipe.RecipeCategoryDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeIngredientDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeInstructionsDTO;
import com.recipemanagementsystem.demo.Entity.*;
import com.recipemanagementsystem.demo.Repository.recipe.*;
import com.recipemanagementsystem.demo.Repository.user.UserRepository;
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

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeInstructionsRepository recipeInstructionsRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public boolean createRecipe(Long userId, RecipeDTO recipeDTO) throws IOException {
        try{

            User user = userRepository.findById(Math.toIntExact(userId))
                    .orElseThrow(() -> new IllegalArgumentException("User not found with Id: " + userId));
            Recipe recipe = new Recipe();
            recipe.setRecipeName(recipeDTO.getRecipeName());
            recipe.setDescription(recipeDTO.getDescription());
            recipe.setTotalTime(recipeDTO.getTotal_time());
            recipe.setPrepTime(recipeDTO.getPrep_time());
            recipe.setCookTime(recipeDTO.getCook_time());
            recipe.setYield(recipeDTO.getYield());
            recipe.setUser(user);

            if (recipeDTO.getImage() != null && !recipeDTO.getImage().isEmpty()) {
                recipe.setRecipeImage(recipeDTO.getImage().getBytes());
            } else {
                recipe.setRecipeImage(null);
            }

            List<Category> categoryList = new ArrayList<>();
            if(recipeDTO.getRecipeCategoriesList() != null && !recipeDTO.getRecipeCategoriesList().isEmpty()){
                for(String categoryName: recipeDTO.getRecipeCategoriesList()){

                    //Check if the category already exists in the database
                    Category category = categoryRepository.findByCategoryName(categoryName);
                    if(category == null){
                        category = new Category();
                        category.setCategoryName(categoryName);
                        category = categoryRepository.save(category);
                    }

                    // Add the category to the list
                    categoryList.add(category);
                }

                // Set the list of categories for the recipe
                recipe.setRecipeCategories(categoryList);
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
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
    public boolean updateRecipeCategories(Long recipeId, List<RecipeCategoryDTO> recipeCategoryDTOList) {
        try {
            // Fetch the recipe by its ID
            Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

            if (optionalRecipe.isPresent()) {
                Recipe recipe = optionalRecipe.get();

                // Retrieve the existing categories of the recipe
                List<Category> existingRecipeCategories = recipe.getRecipeCategories();
                List<Category> updateRecipeCategories = new ArrayList<>(existingRecipeCategories);

                for (RecipeCategoryDTO recipeCategoryDTO : recipeCategoryDTOList) {
                    // Check if the category already exists
                    Category category = categoryRepository.findByCategoryName(recipeCategoryDTO.getRecipeCategoryName());

                    if (category == null) {
                        // If the category does not exist, create a new one
                        category = new Category();
                        category.setCategoryName(recipeCategoryDTO.getRecipeCategoryName());
                        category = categoryRepository.save(category);
                    }

                    // Add the category to the updated categories list if not already present
                    if (!updateRecipeCategories.contains(category)) {
                        updateRecipeCategories.add(category);
                    }
                }

                // Update the recipe with the new list of categories
                recipe.setRecipeCategories(updateRecipeCategories);
                recipeRepository.save(recipe);
                return true;
            } else {
                // Recipe not found
                return false;
            }
        } catch (Exception e) {
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
    }

    @Override
    public boolean deleteRecipeIngredients(Long recipeId, Long recipeIngredientId) {
        if(recipeId == null || recipeIngredientId == null){
            return false;
        }

        try{
            Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
            if(optionalRecipe.isPresent()){
                Optional<RecipeIngredient> recipeIngredients =
                        recipeIngredientRepository.findByRecipeIngredientId(recipeIngredientId);
                if(recipeIngredients.isPresent() &&
                        recipeIngredients.get().getRecipe().getRecipeId().equals(recipeId)){
                    recipeIngredientRepository.deleteById(recipeIngredientId);
                    return true;
                }else {
                    System.out.println("Recipe ingredients not found for ID: " + recipeIngredientId);
                    return false;
                }
            }else{
                System.out.println("Recipe not found for ID: " + recipeId);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRecipeInstructions(Long recipeId, Long recipeInstructionId) {
        if(recipeId == null || recipeInstructionId == null){
            return false;
        }

        try{
            Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
            if(optionalRecipe.isPresent()){
                Optional<RecipeInstructions> recipeInstruction =
                        recipeInstructionsRepository.findByInstructionId(recipeInstructionId);
                if(recipeInstruction.isPresent() &&
                        recipeInstruction.get().getRecipe().getRecipeId().equals(recipeId)){
                    recipeInstructionsRepository.deleteById(recipeInstructionId);
                    return true;
                }else{
                    System.out.println("Recipe instruction not found for ID: " + recipeInstructionId);
                    return false;
                }
            }else {
                System.out.println("Recipe not found for ID: " + recipeId);
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
