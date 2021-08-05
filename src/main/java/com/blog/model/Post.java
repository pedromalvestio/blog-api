package com.blog.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="POST")
public class Post {

    @Id
    @GeneratedValue(generator = "SEQ_POST", strategy= GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_POST", sequenceName="SEQ_POST", allocationSize = 1, initialValue= 1)
    @Column(name="ID")
    private Long id;

    @Column(name="TITLE")
    private String title;

    @Column(name="CONTENT")
    private String content;

    @ManyToMany
    @JoinTable(name = "POST_CATEGORIESX",
               joinColumns = @JoinColumn(name = "POST_IDX"),
                inverseJoinColumns = @JoinColumn(name = "CATEGORIES_IDX"))
    private Set<Category> categories = new HashSet<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
