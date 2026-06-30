package com.makersacademy.audiojakh.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "FAVOURITE_ARTISTS")
public class FavouriteArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "artist_id", nullable = false)
    private String artistId;

    public FavouriteArtist(User user, String artistId) {
        this.user = user;
        this.artistId = artistId;
    }
}
