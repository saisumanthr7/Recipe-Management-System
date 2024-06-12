package com.recipemanagementsystem.demo.Entity;

import com.recipemanagementsystem.demo.Dto.Recipe.RecipeCategoryDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "recipes")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(unique = true)
    private String categoryName;

    @ManyToMany(mappedBy = "recipeCategories")
    private List<Recipe> recipes;


    public RecipeCategoryDTO toRecipeCategoryDTO(){
        RecipeCategoryDTO dto = new RecipeCategoryDTO();
        dto.setRecipeCategoryName(this.categoryName);
        return dto;
    }
}
