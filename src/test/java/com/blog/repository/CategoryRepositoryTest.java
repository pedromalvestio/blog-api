package com.blog.repository;

import com.blog.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@TestPropertySource("file:src/test/resources/test.properties")
@DataJpaTest
@Sql("file:src/test/resources/database.sql")
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repo;

    @Autowired
    private TestEntityManager em;

    @Test
    public void shouldSaveAnCategory(){
        Category category = new Category();
        category.setId(1L);
        category.setName("Category");

        repo.saveAndFlush(category);

        em.detach(category);

        List<Category> categories = repo.findAll();
        assertEquals(1, categories.size());
        assertEquals(1L, categories.get(0).getId());
        assertEquals("Category", categories.get(0).getName());
    }
}
