package com.recipemanagementsystem.demo.Controller;

import com.recipemanagementsystem.demo.Dto.Recipe.RecipeCategoryDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeIngredientDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeInstructionsDTO;
import com.recipemanagementsystem.demo.Services.recipe.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("RMS/api/user")
@RequiredArgsConstructor
public class UserController {

    private final RecipeService recipeService;

    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeDTO>> getAllRecipes(){
        List<RecipeDTO> recipeDTOList = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipeDTOList);
    }

    @GetMapping("/recipeIngredients")
    public ResponseEntity<List<RecipeIngredientDTO>> getAllRecipeIngredients(){
        List<RecipeIngredientDTO> recipeIngredientDTOList = recipeService.getAllRecipeIngredients();
        return ResponseEntity.ok(recipeIngredientDTOList);
    }

    @GetMapping("/recipeInstructions")
    public ResponseEntity<List<RecipeInstructionsDTO>> getAllRecipeInstructions(){
        List<RecipeInstructionsDTO> recipeInstructionsDTOList = recipeService.getAllRecipeInstructions();
        return ResponseEntity.ok(recipeInstructionsDTOList);
    }

    @PostMapping("/createRecipe/{userId}")
    public <T> ResponseEntity<T> createRecipe(@PathVariable Long userId,
                                              @RequestBody RecipeDTO recipeDTO) throws IOException
    {
        boolean success = recipeService.createRecipe(userId, recipeDTO);
            if(success){
                return new  ResponseEntity<>(HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    }

    @PutMapping("/updateRecipe/{recipeId}")
    public <T> ResponseEntity<T> updateRecipe(@PathVariable Long recipeId,
                                              @RequestBody RecipeDTO recipeDTO) {
        boolean success = recipeService.updateRecipe(recipeId, recipeDTO);

        if(success){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("updateRecipeIngredients/{recipeId}")
    public <T> ResponseEntity<T> updateRecipeIngredients(@PathVariable Long recipeId,
                                                         @RequestBody List<RecipeIngredientDTO> recipeIngredientDTOList) {
        boolean success = recipeService.updateRecipeIngredients(recipeId, recipeIngredientDTOList);

        if(success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("updateRecipeInstructions/{recipeId}")
    public  ResponseEntity<?> updateRecipeInstruction(@PathVariable Long recipeId,
                                                         @RequestBody List<RecipeInstructionsDTO> recipeInstructionsDTOList) {
        boolean success = recipeService.updateRecipeInstructions(recipeId, recipeInstructionsDTOList);

        if(success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("updateRecipeCategories/{recipeId}")
    public ResponseEntity<?> updateRecipeCategories(@PathVariable Long recipeId,
                                                    @RequestBody List<RecipeCategoryDTO> recipeCategoryDTO){
        boolean success = recipeService.updateRecipeCategories(recipeId, recipeCategoryDTO);

        if(success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteRecipe/{recipeId}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long recipeId){
        boolean success = recipeService.deleteRecipe(recipeId);

        if(success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteRecipeIngredient/{recipeId}/recipeIngredient/{recipeIngredientId}")
    public ResponseEntity<?> deleteRecipeIngredient(@PathVariable Long recipeId,
                                                    @PathVariable Long recipeIngredientId){
        boolean success = recipeService.deleteRecipeIngredients(recipeId, recipeIngredientId);

        if (success) {
            return ResponseEntity.ok("Recipe Ingredient deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete recipe ingredient.");
        }
    }

    @DeleteMapping("/deleteRecipeInstruction/{recipeId}/recipeInstruction/{recipeInstructionId}")
    public ResponseEntity<?> deleteRecipeInstruction(@PathVariable Long recipeId,
                                                    @PathVariable Long recipeInstructionId){
        boolean success = recipeService.deleteRecipeInstructions(recipeId, recipeInstructionId);

        if (success) {
            return ResponseEntity.ok("Recipe Instruction deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete recipe Instruction.");
        }
    }

    @DeleteMapping("/deleteRecipeCategory/{recipeId}/recipeCateogry/{recipeCategoryId}")
    public ResponseEntity<?> deleteRecipeCategory(@PathVariable Long recipeId,
                                                  @PathVariable Long recipeCategoryId){
        boolean success = recipeService.deleteRecipeCategory(recipeId, recipeCategoryId);

        if (success) {
            return ResponseEntity.ok("Recipe Category deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete category.");
        }
    }
}
