package com.recipemanagementsystem.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeCategoryId;

    private String recipeCategoryName;

    @OneToMany(mappedBy = "recipeCategory")
    private List<Recipe> recipes;
}
