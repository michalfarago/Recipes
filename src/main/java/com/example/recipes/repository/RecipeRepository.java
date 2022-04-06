package com.example.recipes.repository;

import com.example.recipes.entity.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByCategoryIgnoreCaseOrderByCreatedOnDesc(String category);
    List<Recipe> findAllByNameIgnoreCaseContainsOrderByCreatedOnDesc(String name);
}
