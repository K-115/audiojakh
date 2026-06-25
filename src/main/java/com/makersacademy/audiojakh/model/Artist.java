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
@Table(name = "artists")

public class Artist {
    @Id
    private String name;
    @Column(name = "artist_image")
    private String artistImage;

    public Artist(String name, String artistImage) {
        this.name = name;
        this.artistImage = artistImage;
    }
}

