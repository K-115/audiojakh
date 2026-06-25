package com.makersacademy.audiojakh.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "albums")

public class Album {
    @Id
    @Column(name = "spotify_id")
    private String spotifyId;
    private String name;
    @Column(name = "release_date")
    private Date releaseDate;
    @Column(name = "album_cover")
    private String albumCover;

    public Album(String spotifyId, String name, Date releaseDate, String albumCover) {
        this.spotifyId = spotifyId;
        this.name = name;
        this.releaseDate = releaseDate;
        this.albumCover = albumCover;
    }
}

