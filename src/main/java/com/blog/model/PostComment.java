package com.blog.model;

import javax.persistence.*;

@Entity
@Table(name = "COMMENT")
public class PostComment {

    @Id
    @GeneratedValue(generator = "SEQ_POST_COMMENT", strategy= GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_POST_COMMENT", sequenceName="SEQ_POST_COMMENT", allocationSize = 1, initialValue= 1)
    @Column(name="ID")
    private Long id;

    @Column(name = "COMMENT")
    private String comment;

    @ManyToOne
    @JoinColumn(name="POST_ID")
    private Post post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
