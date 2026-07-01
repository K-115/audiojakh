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


    @Column(name = "track_spotify_id")
    private String trackSpotifyId;

    @Column(name = "album_spotify_id")
    private String albumSpotifyId;

    @Column(name = "track_name")
    private String trackName;

    @Column(name = "album_name")
    private String albumName;

    @Column(name = "artist_name")
    private String artistName;


    @Column(name = "image_url")
    private String imageUrl;


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

    public Review(String content, Integer rating, User user, String imageUrl) {
        this.content = content;
        this.rating = rating;
        this.user = user;
        this.imageUrl = imageUrl;
    }

}

