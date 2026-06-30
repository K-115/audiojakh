package com.makersacademy.audiojakh.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "FAVOURITE_ALBUMS")
public class FavouriteAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "spotify_id", nullable = false)
    private String albumId;

    public FavouriteAlbum(User user, String albumId) {
        this.user = user;
        this.albumId = albumId;
    }
}
