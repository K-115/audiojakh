package com.makersacademy.audiojakh.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ARTISTS")

public class Artist {

    @Id
    private String id;

    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "ARTIST_IMAGE")
    private String artistImage;

    private String description; //

    public Artist(String artistName ) {
        this.artistName = artistName;
    }
}
