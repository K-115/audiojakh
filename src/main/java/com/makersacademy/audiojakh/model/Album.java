package com.makersacademy.audiojakh.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ALBUMS")

public class Album {

    @Id
    @Column(name = "SPOTIFY_ID")
    private String spotifyId;

    private String name;

    @Column(name = "RELEASE_DATE")
    private Date releaseDate;

    @Column(name = "ALBUM_COVER")
    private String albumCover;

    public Album(String spotifyId, String name) {
        this.spotifyId = spotifyId;
        this.name = name;
    }
}
