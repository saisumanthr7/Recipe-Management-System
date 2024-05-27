package com.recipemanagementsystem.demo.Dto.Recipe;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RecipeInstructionsDTO {
    private int stepNumber;
    private String instruction;
    private MultipartFile instructionImage;
}
