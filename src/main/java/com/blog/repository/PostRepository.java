package com.blog.repository;

import com.blog.model.Category;
import com.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    public List<Post> findByCategoriesIn(Set<Category> categories);

    @Query("SELECT p "
            + " FROM Post p "
            + " WHERE not exists elements(p.categories)")
    public List<Post> findAllWithoutCategory();
}
