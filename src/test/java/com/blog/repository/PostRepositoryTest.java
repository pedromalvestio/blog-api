package com.blog.repository;

import com.blog.entity.CategoryTestEntity;
import com.blog.entity.PostTestEntity;
import com.blog.model.Category;
import com.blog.model.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource("file:src/test/resources/test.properties")
@DataJpaTest
@Sql("file:src/test/resources/database.sql")
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void shouldSaveAnPostWithCategories(){

        Category category1 = CategoryTestEntity.createGenericCategory();
        category1 = testEntityManager.merge(category1);
//        postCategory1 = testEntityManager.persistAndFlush(postCategory1);

        Category category2 = CategoryTestEntity.createGenericCategory();
        category2 = testEntityManager.merge(category2);
//        postCategory2 = testEntityManager.persistAndFlush(postCategory2);
        Category postCategory1 = new Category();
        postCategory1.setId(category1.getId());
        Category postCategory2 = new Category();
        postCategory2.setId(category2.getId());

        Post createPost = postRepository.save(PostTestEntity.createGenericPostWithCategory(Set.of(postCategory1, postCategory2)));

        List<Post> postList = postRepository.findAll();
        Category finalPostCategory1 = category1;
        Category testCategory1 = postList.get(0).getCategories()
                .stream().filter(category -> category.getId().equals(finalPostCategory1.getId())).findFirst().orElse(new Category());
        Category finalPostCategory2 = category2;
        Category testCategory2 = postList.get(0).getCategories()
                .stream().filter(category -> category.getId().equals(finalPostCategory2.getId())).findFirst().orElse(new Category());
        assertEquals(1, postList.size());
        assertEquals(createPost.getId(), postList.get(0).getId());
        assertEquals(createPost.getTitle(), postList.get(0).getTitle());
        assertEquals(createPost.getContent(), postList.get(0).getContent());
        assertEquals(category1.getName(), testCategory1.getName());
        assertEquals(category2.getName(), testCategory2.getName());
    }

    @Test
    public void shouldSaveAnPostWithoutCategory(){
        Post createPost = postRepository.save(PostTestEntity.createGenericPost());;

        List<Post> postList = postRepository.findAll();
        assertEquals(1, postList.size());
        assertEquals(createPost.getId(), postList.get(0).getId());
        assertEquals(createPost.getTitle(), postList.get(0).getTitle());
        assertEquals(createPost.getContent(), postList.get(0).getContent());
        assertEquals(createPost.getCategories(), new HashSet<Category>());
    }

    @Test
    public void shouldNotSaveAnPostWithInvalidCategory(){
        Post createPost = PostTestEntity.createGenericPost();
        Category invalidCategory = CategoryTestEntity.createGenericCategory();
        createPost.setCategories(Set.of(invalidCategory));

        assertThrows(JpaObjectRetrievalFailureException.class, () -> {
            postRepository.save(createPost);
        });
    }

    @Test
    public void shouldReturnPostsOfSpecificCategory(){
        Category postCategory = CategoryTestEntity.createGenericCategory();

        postCategory = testEntityManager.merge(postCategory);
        postCategory = testEntityManager.persistAndFlush(postCategory);

        Post createPost1 = postRepository.save(PostTestEntity.createGenericPostWithCategory(Set.of(postCategory)));
        postRepository.save(PostTestEntity.createGenericPost());
        Post createPost2 = postRepository.save(PostTestEntity.createGenericPostWithCategory(Set.of(postCategory)));

        List<Post> postList = postRepository.findByCategoriesIn(Set.of(postCategory));
        Category finalPostCategory1 = postCategory;
        Category category1 = postList.get(0).getCategories()
                .stream().filter(category -> category.getId().equals(finalPostCategory1.getId())).findFirst().orElse(new Category());

        assertEquals(2, postList.size());
        assertEquals(createPost1.getId(), postList.get(0).getId());
        assertTrue(createPost1.getCategories().contains(postCategory));
        assertEquals(createPost2.getId(), postList.get(1).getId());
        assertTrue(createPost2.getCategories().contains(postCategory));
    }

    @Test
    public void shouldReturnPostsWithoutCategory(){

        Category postCategory = CategoryTestEntity.createGenericCategory();

        postCategory = testEntityManager.merge(postCategory);
        postCategory = testEntityManager.persistAndFlush(postCategory);

        Post createPost1 = postRepository.save(PostTestEntity.createGenericPost());
        postRepository.save(PostTestEntity.createGenericPostWithCategory(Set.of(postCategory)));
        Post createPost2 = postRepository.save(PostTestEntity.createGenericPost());
        postRepository.save(PostTestEntity.createGenericPostWithCategory(Set.of(postCategory)));


        List<Post> postList = postRepository.findAllWithoutCategory();
        assertEquals(2, postList.size());
        assertEquals(createPost1.getId(), postList.get(0).getId());
        assertEquals(new HashSet<Category>(), postList.get(0).getCategories());
        assertEquals(createPost2.getId(), postList.get(1).getId());
        assertEquals(new HashSet<Category>(), postList.get(1).getCategories());
    }
}
