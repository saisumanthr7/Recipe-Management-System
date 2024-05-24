package com.recipemanagementsystem.demo.Entity;


import com.recipemanagementsystem.demo.Dto.Recipe.RecipeDTO;
import com.recipemanagementsystem.demo.Dto.Recipe.RecipeIngredientDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Base64;
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

    private int prep_time;

    private int cook_time;

    private String yield;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipeCategory_id")
    private RecipeCategory recipeCategory;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeIngredient> recipeIngredientsList;

//    private Method method;

//    private Cuisine cuisine;

    public RecipeDTO getRecipeDTO() {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setRecipeId(this.getRecipeId());
        recipeDTO.setRecipeName(this.getRecipeName());
        recipeDTO.setDescription(this.getDescription());
        recipeDTO.setTotal_time(this.getTotalTime());
        recipeDTO.setPrep_time(this.getPrep_time());
        recipeDTO.setCook_time(this.getCook_time());
        recipeDTO.setYield(this.getYield());
        if(this.getImage() != null){
            recipeDTO.setReturnedImage(this.getImage());
        }
        if(this.recipeIngredientsList != null){
            List<RecipeIngredientDTO> recipeIngredientDTOList = this.recipeIngredientsList.stream()
                    .map(RecipeIngredient::getRecipeIngredientDTO)
                    .collect(Collectors.toList());
            recipeDTO.setRecipeIngredientDTOList(recipeIngredientDTOList);
        }
        return recipeDTO;
    }
}
