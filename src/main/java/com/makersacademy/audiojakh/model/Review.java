package com.makersacademy.audiojakh.model;

import jakarta.persistence.*;
        import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", referencedColumnName = "SPOTIFY_ID")
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", referencedColumnName = "SPOTIFY_ID")
    private Album album;

    private Integer likes = 0;

    @Column(name = "date_of_review", insertable = false, updatable = false)
    private LocalDateTime dateOfReview;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "review")
    @OrderBy("date_of_comment ASC")
    private List<Comment> comments;


    public Review() {}

    public Review(String content, Integer rating, User user) {
        this.content = content;
        this.rating = rating;
        this.user = user;
    }
}

