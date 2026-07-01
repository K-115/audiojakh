package com.makersacademy.audiojakh.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(name = "likes")
    private Integer likes = 0;

    @Column(name = "date_of_comment", nullable = false)
    private LocalDateTime dateOfComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review", nullable = false)
    private Review review;

    @PrePersist
    protected void onCreate() {
        this.dateOfComment = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getLikes() { return likes; }
    public void setLikes(Integer likes) { this.likes = likes; }

    public LocalDateTime getDateOfComment() { return dateOfComment; }
    public void setDateOfComment(LocalDateTime dateOfComment) { this.dateOfComment = dateOfComment; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Review getReview() { return review; }
    public void setReview(Review review) { this.review = review; }
}
