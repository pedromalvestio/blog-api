package com.blog.controller;

import com.blog.entity.PostTestEntity;
import com.blog.model.Post;
import com.blog.model.PostComment;
import com.blog.repository.CategoryRepository;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
import com.blog.service.CategoryService;
import com.blog.service.CommentService;
import com.blog.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = {CommentController.class, CommentService.class, PostService.class, CategoryService.class})
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Captor
    private ArgumentCaptor<PostComment> argumentCaptor;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldCreateAnComment() throws Exception {

        Post post = PostTestEntity.createGenericPost();
        Long commentId = nextLong(1,100);
        PostComment inputComment = new PostComment();
        inputComment.setComment(randomAlphanumeric(300));

        when(commentRepository.save(argumentCaptor.capture())).thenAnswer(i -> {
            PostComment comment = i.getArgument(0, PostComment.class);
            comment.setId(commentId);
            return comment;
        });

        when(postRepository.findById(post.getId())).thenReturn(java.util.Optional.of(post));

        ResultActions postResult = mockMvc.perform(post("/post/{postId}/comment", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputComment)));

        postResult.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(commentId))
                .andExpect(jsonPath("comment").value(inputComment.getComment()))
                .andExpect(jsonPath("post.id").value(post.getId()));
    }

    @Test
    public void shouldNotCreateCommentForInvalidPost() throws Exception {
        Long commentId = nextLong(1,100), postId = nextLong(1,100);
        PostComment inputComment = new PostComment();
        inputComment.setComment(randomAlphanumeric(300));

        when(commentRepository.save(argumentCaptor.capture())).thenAnswer(i -> {
            PostComment comment = i.getArgument(0, PostComment.class);
            comment.setId(commentId);
            return comment;
        });

        ResultActions postResult = mockMvc.perform(post("/post/{postId}/comment", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputComment)));

        postResult.andDo(print()).andExpect(status().isNotFound());
    }
}
