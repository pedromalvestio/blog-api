package com.blog.entity;

import com.blog.model.Category;
import com.blog.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;

public class PostTestEntity {

    public static Post createGenericPost(){
        Post post = new Post();
        post.setId(nextLong(1,100));
        post.setTitle(randomAlphanumeric(30));
        post.setContent(randomAlphanumeric(200));

        return post;
    }

    public static Post createGenericPostWithCategory(Set<Category> categories){
        Post post = createGenericPost();
        post.setCategories(categories);
        return post;
    }

    public static List<Post> createGenericPostList(){
        List<Post> posts = new ArrayList<>();
        posts.add(PostTestEntity.createGenericPost());
        posts.add(PostTestEntity.createGenericPost());

        return posts;
    }

    public static List<Post> createGenericPostListWithCategory(Category category){
        List<Post> posts = new ArrayList<>();
        posts.add(PostTestEntity.createGenericPostWithCategory(Set.of(category)));
        posts.add(PostTestEntity.createGenericPostWithCategory(Set.of(category)));

        return posts;
    }
}
