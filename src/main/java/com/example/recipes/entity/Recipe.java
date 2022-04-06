package com.example.recipes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name may not be blank.")
    private String name;

    @NotBlank(message = "Description may not be blank.")
    private String description;

    @ElementCollection
    @NotEmpty(message = "Ingredients may not be empty.")
    private List<String> ingredients;

    @ElementCollection
    @NotEmpty(message = "Directions may not be empty.")
    private List<String> directions;

    @NotBlank(message = "Category may not be blank.")
    private String category;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
