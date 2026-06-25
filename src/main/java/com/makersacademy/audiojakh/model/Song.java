package com.makersacademy.audiojakh.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tracks")

public class Song {
    @Id
    @Column(name = "spotify_id")
    private String spotifyId;
    private String name;
    @Column(name = "album_id")
    private String albumID;
    @Column(name = "duration_ms")
    private Integer durationMS;
    private String genre;

    public Song(String spotifyId, String name, String albumID, Integer durationMS, String genre ) {
        this.spotifyId = spotifyId;
        this.name = name;
        this.albumID = albumID;
        this.durationMS = durationMS;
        this.genre = genre;
    }

}
