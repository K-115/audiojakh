package com.makersacademy.audiojakh.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Long poster;
    @Column(name = "review")
    private Long reviewId;
    private Long likes = 0L;
    
    @Column(name = "date_of_comment")
    private LocalDateTime dateOfComment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "poster", insertable = false, updatable = false)
    private User user;
}
