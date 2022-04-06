package com.example.recipes.security;

import com.example.recipes.entity.Recipe;
import com.example.recipes.entity.User;
import com.example.recipes.repository.UserRepository;
import com.example.recipes.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    private RecipeService recipeService;

    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository, RecipeService recipeService) {
        this.userRepository = userRepository;
        this.recipeService = recipeService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsImpl(user);
    }

    public boolean isOwner(long id, UserDetailsImpl userDetails) {
        Recipe recipe = recipeService.getRecipeById(id);
        return recipe.getUser().getEmail().equals(userDetails.getUsername());
    }
}
