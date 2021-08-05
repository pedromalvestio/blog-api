package com.blog.service;

import com.blog.exception.CategoryNotFoundException;
import com.blog.model.Category;
import com.blog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public Category create(Category category) {
        return repository.save(category);
    }

    public Category getById(Long id) {
        Optional<Category> category = repository.findById(id);
        if (category.isEmpty()) {
            throw new CategoryNotFoundException();
        }
        return category.get();
    }

    public Set<Category> getCategories(Set<Category> categories){
        if (categories.isEmpty()) throw new CategoryNotFoundException();
        Set<Category> returnCategories = new HashSet<>();
        for (Category c : categories){
            returnCategories.add(getById(c.getId()));
        }
        return returnCategories;
    }
}
