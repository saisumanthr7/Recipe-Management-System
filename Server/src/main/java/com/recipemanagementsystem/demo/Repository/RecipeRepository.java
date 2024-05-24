package com.recipemanagementsystem.demo.Repository;

import com.recipemanagementsystem.demo.Entity.Recipe;
import com.recipemanagementsystem.demo.Entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findRecipeByRecipeId(long recipeId);
}
