package com.blog.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="CATEGORY")
public class Category {

    @Id
    @GeneratedValue(generator = "SEQ_CATEGORY", strategy= GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_CATEGORY", sequenceName="SEQ_CATEGORY", allocationSize = 1, initialValue= 1)
    @Column(name="ID")
    private Long id;

    @Column(name="NAME")
    private String name;

    @ManyToMany
    private Set<Post> posts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}
