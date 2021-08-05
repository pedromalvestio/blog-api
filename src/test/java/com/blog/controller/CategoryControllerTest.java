package com.blog.controller;

import com.blog.model.Category;
import com.blog.repository.CategoryRepository;
import com.blog.service.CategoryService;
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

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = {CategoryController.class, CategoryService.class})
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository repo;

    @Captor
    private ArgumentCaptor<Category> argumentCaptor;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldCreateCategory() throws Exception {

        Category inputCategory = new Category();
        inputCategory.setName("Input Category");

        when(repo.save(argumentCaptor.capture())).thenAnswer(i -> {
            Category category = i.getArgument(0, Category.class);
            category.setId(1L);
            return category;
        });

        ResultActions post = mockMvc.perform(post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputCategory)));

        post.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value(inputCategory.getName()));

        Category category = argumentCaptor.getValue();

        assertEquals(1L, category.getId());
        assertEquals(inputCategory.getName(), category.getName());
    }

    @Test
    public void shouldReturnCategoryById() throws Exception {

        Category category = new Category();
        category.setName(randomAlphanumeric(25));
        category.setId(nextLong(1,100));
        Optional<Category> categoryOpt = Optional.of(category);
        when(repo.findById(category.getId())).thenReturn(categoryOpt);

        ResultActions get = mockMvc.perform(get("/category/{id}",category.getId()));

        get.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("id").value(category.getId()))
                .andExpect(jsonPath("name").value(category.getName()));

    }

    @Test
    public void shouldReturnEmptyCategory() throws Exception {
        ResultActions get = mockMvc.perform(get("/category/{id}",1));

        get.andDo(print()).andExpect(status().isNotFound());
    }
}
