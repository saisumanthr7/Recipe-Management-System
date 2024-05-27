package com.recipemanagementsystem.demo.Dto.Recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RecipeIngredientDTO {
    private Long ingredientId;
    private String ingredientName;
    private double quantity;
    private String unit;
}
