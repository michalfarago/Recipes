package com.example.recipes.controller;

import com.example.recipes.entity.Recipe;
import com.example.recipes.security.UserDetailsImpl;
import com.example.recipes.service.RecipeService;
import com.example.recipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class ApiController {

    private RecipeService recipeService;
    private UserService userService;

    @Autowired
    public ApiController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Recipe getRecipe(@PathVariable long id){
        return  recipeService.getRecipeById(id);
    }

    @PostMapping()
    public Map<String,Long> newRecipe(@AuthenticationPrincipal UserDetailsImpl details, @RequestBody @Valid Recipe newRecipe) {
        newRecipe.setUser(userService.findById(details.getUsername()));
        return recipeService.saveRecipe(newRecipe);
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("@userDetailServiceImpl.isOwner(#id, principal)")
    public void updateRecipe(@RequestBody Recipe recipeToUpdated, @PathVariable long id){
        recipeService.updateRecipe(recipeToUpdated,id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("@userDetailServiceImpl.isOwner(#id, principal)")
    public void deleteRecipe(@PathVariable long id){
        recipeService.deleteRecipe(id);
    }

    @GetMapping("/search")
    public List<Recipe> searchRecipes(@RequestParam(required = false) String category, @RequestParam(required = false) String name){
        if(category == null && name == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong query parameter.");
        } else if (name == null) {
            return recipeService.getRecipesByCategory(category);
        } else if (category == null){
            return recipeService.getRecipesByName(name);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong query parameter.");
        }
    }
}
