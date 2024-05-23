package com.recipemanagementsystem.demo.Repository;

import com.recipemanagementsystem.demo.Entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
