package com.recipemanagementsystem.demo.Repository.recipe;

import com.recipemanagementsystem.demo.Entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {


}
