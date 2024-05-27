package com.recipemanagementsystem.demo.Repository.recipe;

import com.recipemanagementsystem.demo.Entity.RecipeInstructions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeInstructionsRepository extends JpaRepository<RecipeInstructions, Long> {
    Optional<RecipeInstructions> findInstructionByStepNumber(int stepNumber);
}
