package com.recipemanagementsystem.demo.Repository.recipe;

import com.recipemanagementsystem.demo.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByCategoryId(Long categoryId);
    Category findByCategoryName(String categoryName);
}
