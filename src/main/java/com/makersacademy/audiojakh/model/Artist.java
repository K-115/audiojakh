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
    @GeneratedValue
    private Long id;

    private String name;

    @Column(name = "ARTIST_IMAGE")
    private String artistImage;

    public Artist(String name) {
        this.name = name;
    }
}
