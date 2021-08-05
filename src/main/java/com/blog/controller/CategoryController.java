package com.blog.controller;

import com.blog.model.Category;
import com.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category){

        service.create(category);

        return ResponseEntity.ok(category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }

}
