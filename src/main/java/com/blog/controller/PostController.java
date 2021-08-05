package com.blog.controller;

import com.blog.model.Post;
import com.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService service;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){
        return ResponseEntity.ok(service.getAllPosts());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Post>> getPostsByCategory(@PathVariable Long categoryId){
        return ResponseEntity.ok(service.findPostsByCategory(categoryId));
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post){

        Post createdPost = service.create(post);

        return ResponseEntity.ok(createdPost);
    }

}
