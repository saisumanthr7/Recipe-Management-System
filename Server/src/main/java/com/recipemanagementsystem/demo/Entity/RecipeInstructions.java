package com.recipemanagementsystem.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeInstructions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instructionId;
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    private int stepNumber;

    @Lob
    private String instruction;

    private byte[] instructionImage;
}
