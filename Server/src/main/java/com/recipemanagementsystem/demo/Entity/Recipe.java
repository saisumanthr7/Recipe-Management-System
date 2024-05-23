package com.recipemanagementsystem.demo.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
}
