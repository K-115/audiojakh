package com.makersacademy.audiojakh.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TRACKS")

public class Track {

    @Id
    @Column(name = "SPOTIFY_ID")
    private String spotifyId;

    private String name;

    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "ALBUM_ID")
    private String albumId;

    @Column(name = "DURATION_MS")
    private Integer durationMs;

    private String genre;

    @Column(name = "ARTIST_ID")
    private String artistId;

    public Track(String spotifyId, String name) {
        this.spotifyId = spotifyId;
        this.name = name;
    }

    // handles display time format of milliseconds > mm:ss
    public String getFormattedDuration() {
        if (durationMs == null) return "";
        int totalSeconds = durationMs / 1000;
        return String.format("%d:%02d", totalSeconds / 60, totalSeconds % 60);
    }


}
