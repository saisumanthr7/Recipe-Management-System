package com.recipemanagementsystem.demo.Controller;

import com.recipemanagementsystem.demo.Dto.Recipe.RecipeDTO;
import com.recipemanagementsystem.demo.Services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
}
