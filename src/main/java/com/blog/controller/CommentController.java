package com.blog.controller;

import com.blog.model.PostComment;
import com.blog.repository.CommentRepository;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<PostComment> createPost(@PathVariable Long postId, @RequestBody PostComment comment){
        PostComment postComment = commentService.create(postId, comment);
        return ResponseEntity.ok(postComment);
    }
}
