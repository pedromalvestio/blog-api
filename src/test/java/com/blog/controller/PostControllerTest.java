package com.blog.controller;

import com.blog.entity.CategoryTestEntity;
import com.blog.entity.PostTestEntity;
import com.blog.model.Category;
import com.blog.model.Post;
import com.blog.repository.CategoryRepository;
import com.blog.repository.PostRepository;
import com.blog.service.CategoryService;
import com.blog.service.PostService;
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

import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = {PostController.class, PostService.class, CategoryService.class})
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldCreatePostWithoutCategory() throws Exception {

        Post createPost = PostTestEntity.createGenericPost();

        when(postRepository.save(postArgumentCaptor.capture())).thenAnswer(i -> {
            Post post = i.getArgument(0, Post.class);
            post.setId(createPost.getId());
            return post;
        });

        ResultActions post = mockMvc.perform(post("/post")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createPost)));

        post.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(createPost.getId()))
            .andExpect(jsonPath("title").value(createPost.getTitle()))
            .andExpect(jsonPath("content").value(createPost.getContent()))
        ;
    }

    @Test
    public void shouldCreatePostWithCategory() throws Exception {
        Category getCategory = CategoryTestEntity.createGenericCategory();

        when(categoryRepository.findById(getCategory.getId())).thenReturn(Optional.of(getCategory));

        Category postCategory = new Category();
        postCategory.setId(getCategory.getId());
        Post createPost = PostTestEntity.createGenericPost();
        createPost.setCategories(Set.of(postCategory));

        when(postRepository.save(postArgumentCaptor.capture())).thenAnswer(i -> {
            Post post = i.getArgument(0, Post.class);
            post.setId(createPost.getId());
            return post;
        });

        ResultActions post = mockMvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPost)));

        post.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(createPost.getId()))
            .andExpect(jsonPath("title").value(createPost.getTitle()))
            .andExpect(jsonPath("content").value(createPost.getContent()))
            .andExpect(jsonPath("categories.[0].id").value(getCategory.getId()))
            .andExpect(jsonPath("categories.[0].name").value(getCategory.getName()))
        ;
    }

    @Test
    public void shouldNotCreatePostWithInvalidCategory() throws Exception {
        Category getCategory = CategoryTestEntity.createGenericCategory();
        Post createPost = PostTestEntity.createGenericPost();
        createPost.setCategories(Set.of(getCategory));

        when(postRepository.save(postArgumentCaptor.capture())).thenAnswer(i -> {
            Post post = i.getArgument(0, Post.class);
            post.setId(createPost.getId());
            return post;
        });

        ResultActions post = mockMvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPost)));

        post.andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnAllPosts() throws Exception {

        when(postRepository.findAll()).thenReturn(PostTestEntity.createGenericPostList());

        ResultActions getPosts = mockMvc.perform(get("/post/"));
        getPosts.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2));
    }

    @Test
    public void shouldReturnPostsByCategory() throws Exception {
        Category postsCategory = CategoryTestEntity.createGenericCategory();
        Optional<Category> postCategoryOpt = Optional.of(postsCategory);

        when(categoryRepository.findById(postsCategory.getId())).thenReturn(postCategoryOpt);

        when(postRepository.findByCategoriesIn(Set.of(postsCategory))).thenReturn(PostTestEntity.createGenericPostListWithCategory(postsCategory));


        ResultActions getPosts = mockMvc.perform(get("/post/category/{categoryId}", postsCategory.getId()));
        getPosts.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].categories.[0].id").value(postsCategory.getId()))
                .andExpect(jsonPath("[1].categories.[0].id").value(postsCategory.getId()))
                ;
    }
}
