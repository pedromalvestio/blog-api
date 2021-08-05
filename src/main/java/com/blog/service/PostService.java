package com.blog.service;

import com.blog.exception.CategoryNotFoundException;
import com.blog.exception.PostNotFoundException;
import com.blog.model.Category;
import com.blog.model.Post;
import com.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    @Autowired
    private CategoryService categoryService;

    public Post create(Post post) {

        if (!post.getCategories().isEmpty())
            post.setCategories(categoryService.getCategories(post.getCategories()));

        return repository.save(post);
    }

    public List<Post> getAllPosts() {
        return repository.findAll();
    }

    public List<Post> findPostsByCategory(Long categoryId) {
        Category category = categoryService.getById(categoryId);
        return repository.findByCategoriesIn(Set.of(category));
    }

    public Post findById(Long postId) {
        Optional<Post> post = repository.findById(postId);
        if (post.isEmpty()) {
            throw new PostNotFoundException();
        }
        return post.get();
    }
}
