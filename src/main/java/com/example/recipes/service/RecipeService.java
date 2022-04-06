package com.example.recipes.service;

import com.example.recipes.entity.Recipe;
import com.example.recipes.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RecipeService {

    private RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public Map<String,Long> saveRecipe(Recipe recipe){
        recipeRepository.save(recipe);
        return Map.of("id",recipe.getId());
    }

    public Recipe getRecipeById(long id){
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No recipe with ID %d found.",id)));
    }

    @Transactional
    public void updateRecipe(Recipe newRecipe, long id){
        Optional<Recipe> oldRecipe = recipeRepository.findById(id);
        oldRecipe.ifPresentOrElse((recipe) -> {
                newRecipe.setId(id);
                recipeRepository.save(newRecipe);
        }, () -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No recipe with ID %d found.",id)); });
    }

    @Transactional
    public void deleteRecipe(long id){
        Optional<Recipe> oldRecipe = recipeRepository.findById(id);
        oldRecipe.ifPresentOrElse((recipe) -> recipeRepository.deleteById(id)
        , () -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No recipe with ID %d found.",id)); });
    }

    public List<Recipe> getRecipesByName(String name){
        return recipeRepository.findAllByNameIgnoreCaseContainsOrderByCreatedOnDesc(name);
    }

    public List<Recipe> getRecipesByCategory(String category){
        return recipeRepository.findAllByCategoryIgnoreCaseOrderByCreatedOnDesc(category);
    }

    public Page<Recipe> getRecipesByPage(int page, int size){
        Pageable recipes = PageRequest.of(page, size);
        return recipeRepository.findAll(recipes);
    }
}
