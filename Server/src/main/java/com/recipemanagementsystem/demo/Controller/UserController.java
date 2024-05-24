package com.recipemanagementsystem.demo.Controller;

import com.recipemanagementsystem.demo.Dto.Recipe.RecipeDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeIngredientDTO;
import com.recipemanagementsystem.demo.Services.user.UserService;
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

    private final UserService userService;

    @PostMapping("/createRecipe")
    public <T> ResponseEntity<T> createRecipe(@RequestBody RecipeDTO recipeDTO) throws IOException{
        boolean success = userService.createRecipe(recipeDTO);
            if(success){
                return new  ResponseEntity<>(HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    }

    @PutMapping("/updateRecipe/{recipeId}")
    public <T> ResponseEntity<T> updateRecipe(@PathVariable Long recipeId,@RequestBody RecipeDTO recipeDTO) throws IOException {
        boolean success = userService.updateRecipe(recipeId, recipeDTO);

        if(success){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("updateRecipeIngredients/{recipeId}")
    public <T> ResponseEntity<T> updateRecipeIngredients(@PathVariable Long recipeId, @RequestBody List<RecipeIngredientDTO> recipeIngredientDTOList) throws  IOException {
        boolean success = userService.updateRecipeIngredients(recipeId, recipeIngredientDTOList);

        if(success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
