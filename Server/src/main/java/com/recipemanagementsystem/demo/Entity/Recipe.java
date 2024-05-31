package com.recipemanagementsystem.demo.Entity;


import com.recipemanagementsystem.demo.Dto.Recipe.RecipeCategoryDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeIngredientDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeInstructionsDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "recipeIngredientList")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipeId")
    private Long recipeId;

    private String recipeName;

    @Column(columnDefinition="TEXT")
    private String description;

    private int totalTime;

    private int prepTime;

    private int cookTime;

    private String yield;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] recipeImage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "recipe_category_mapping",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> recipeCategories;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeIngredient> recipeIngredientsList;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeInstructions> recipeInstructionsList;

//    private Method method;

//    private Cuisine cuisine;

    public RecipeDTO getRecipeDTO() {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setRecipeId(this.getRecipeId());
        recipeDTO.setRecipeName(this.getRecipeName());
        recipeDTO.setDescription(this.getDescription());
        recipeDTO.setTotal_time(this.getTotalTime());
        recipeDTO.setPrep_time(this.getPrepTime());
        recipeDTO.setCook_time(this.getCookTime());
        recipeDTO.setYield(this.getYield());
        if(this.getRecipeImage() != null){
            recipeDTO.setReturnedImage(this.getRecipeImage());
        }
        if(this.recipeIngredientsList != null){
            List<RecipeIngredientDTO> recipeIngredientDTOList = this.recipeIngredientsList.stream()
                    .map(RecipeIngredient::getRecipeIngredientDTO)
                    .collect(Collectors.toList());
            recipeDTO.setRecipeIngredientDTOList(recipeIngredientDTOList);
        }

        if(this.recipeInstructionsList != null){
            List<RecipeInstructionsDTO> recipeInstructionsDTOList = this.recipeInstructionsList.stream()
                    .map(RecipeInstructions::getRecipeInstructionsDTO).toList();
            recipeDTO.setRecipeInstructionsDTOList(recipeInstructionsDTOList);
        }


        return recipeDTO;
    }
}
