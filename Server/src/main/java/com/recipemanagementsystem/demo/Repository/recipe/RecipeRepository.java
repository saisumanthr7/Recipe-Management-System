package com.recipemanagementsystem.demo.Repository.recipe;

import com.recipemanagementsystem.demo.Entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findRecipeByRecipeId(long recipeId);
}
