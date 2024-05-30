package com.recipemanagementsystem.demo.Dto.Recipe;

import com.recipemanagementsystem.demo.Entity.Category;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RecipeDTO {

    @Id
    private Long recipeId;

    private String recipeName;

    private String description;

    private int total_time;

    private int prep_time;

    private int cook_time;

    private String yield;

    private MultipartFile image;

    private byte[] returnedImage;

    private List<String> recipeCategoriesList;

    private List<RecipeIngredientDTO> recipeIngredientDTOList;

    private List<RecipeInstructionsDTO> recipeInstructionsDTOList;

}
