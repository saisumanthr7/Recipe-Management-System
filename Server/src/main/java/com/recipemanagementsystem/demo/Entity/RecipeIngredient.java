package com.recipemanagementsystem.demo.Entity;

import com.recipemanagementsystem.demo.Dto.Recipe.RecipeIngredientDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "recipe")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeIngredientId;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private double quantity;

    private String unit;

    public RecipeIngredientDTO getRecipeIngredientDTO(){
        RecipeIngredientDTO recipeIngredientDTO = new RecipeIngredientDTO();
        recipeIngredientDTO.setIngredientId(this.ingredient.getIngredientId());
        recipeIngredientDTO.setIngredientName(this.ingredient.getIngredientName());
        recipeIngredientDTO.setQuantity(this.getQuantity());
        recipeIngredientDTO.setUnit(this.getUnit());
        return recipeIngredientDTO;
    }
}
