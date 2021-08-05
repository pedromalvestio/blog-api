package com.blog.service;

import com.blog.model.Post;
import com.blog.model.PostComment;
import com.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    public PostComment create(Long postId, PostComment comment) {
        Post post = postService.findById(postId);
        comment.setPost(post);
        PostComment savedComment = commentRepository.save(comment);
        return savedComment;
    }
}
