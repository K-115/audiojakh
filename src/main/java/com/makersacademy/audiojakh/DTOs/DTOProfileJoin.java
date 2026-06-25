package com.makersacademy.audiojakh.DTOs;

import com.makersacademy.audiojakh.model.*;
import lombok.Getter;

import java.util.List;

@Getter
public class DTOProfileJoin {
    private User user;
//    private List<Review> reviews;
    private List<Album> favouriteAlbums;
    private List<Song> favouriteSongs;
    private List<Artist> favouriteArtists;
    private boolean isOwnProfile;

    public DTOProfileJoin(User user, List<Album> favouriteAlbums, List<Song> favouriteSongs, List<Artist> favouriteArtists, boolean isOwnProfile) {
        this.user = user;
//        this.reviews = reviews;
        this.favouriteAlbums = favouriteAlbums;
        this.favouriteSongs = favouriteSongs;
        this.favouriteArtists = favouriteArtists;
        this.isOwnProfile = isOwnProfile;
    }
}



