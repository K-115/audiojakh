package com.makersacademy.audiojakh.DTOs;

import com.makersacademy.audiojakh.model.*;
import lombok.Getter;

import java.util.List;

@Getter
public class DTOProfileJoin {
    private User user;
    private List<Album> favouriteAlbums;
    private List<Track> favouriteSongs;
    private List<Artist> favouriteArtists;
    private List<Review> reviews;
    private boolean isOwnProfile;
    private boolean isFollowing;
    private long followerCount;
    private long followingCount;
    private long reviewCount;

    public DTOProfileJoin(User user, List<Album> favouriteAlbums, List<Track> favouriteSongs,
                          List<Artist> favouriteArtists, List<Review> reviews, boolean isOwnProfile, boolean isFollowing,
                          long followerCount, long followingCount, long reviewCount) {
        this.user = user;
        this.reviews = reviews;
        this.favouriteAlbums = favouriteAlbums;
        this.favouriteSongs = favouriteSongs;
        this.favouriteArtists = favouriteArtists;
        this.isOwnProfile = isOwnProfile;
        this.isFollowing = isFollowing;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.reviewCount = reviewCount;
    }
}



