//package com.makersacademy.audiojakh.DTOs;
//
//import com.makersacademy.audiojakh.model.*;
//import lombok.Getter;
//
//import java.util.List;
//
//@Getter
//public class DTOProfileJoin {
//    private User user;
//    private List<Album> favouriteAlbums;
//    private List<Track> favouriteSongs;
//    private List<Artist> favouriteArtists;
//    private List<Review> reviews;
//    private boolean isOwnProfile;
//    private boolean isFollowing;
//    private long followerCount;
//    private long followingCount;
//    private long reviewCount;
//
//    public DTOProfileJoin(User user, List<Album> favouriteAlbums, List<Track> favouriteSongs,
//                          List<String> favouriteArtists, List<Review> reviews, boolean isOwnProfile, boolean isFollowing,
//                          long followerCount, long followingCount, long reviewCount) {
//        this.user = user;
//        this.reviews = reviews;
//        this.favouriteAlbums = favouriteAlbums;
//        this.favouriteSongs = favouriteSongs;
//        this.favouriteArtists = favouriteArtists;
//        this.isOwnProfile = isOwnProfile;
//        this.isFollowing = isFollowing;
//        this.followerCount = followerCount;
//        this.followingCount = followingCount;
//        this.reviewCount = reviewCount;
//    }
//}
//
//
//

package com.makersacademy.audiojakh.DTOs;

import com.makersacademy.audiojakh.model.Review;
import com.makersacademy.audiojakh.model.User;
import lombok.Getter;
import lombok.Setter;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

@Getter
@Setter
public class DTOProfileJoin {
    private final User user;
    private final List<Review> reviews;
    private final boolean isOwnProfile;
    private final boolean isFollowing;
    private final long followerCount;
    private final long followingCount;
    private final long reviewCount;

    private final List<Album> favouriteAlbums;
    private final List<Track> favouriteSongs;
    private final List<Artist> favouriteArtists;

    public DTOProfileJoin(User user,
                          List<Album> favouriteAlbums,
                          List<Track> favouriteSongs,
                          List<Artist> favouriteArtists,
                          List<Review> reviews,
                          boolean isOwnProfile,
                          boolean isFollowing,
                          long followerCount,
                          long followingCount,
                          long reviewCount) {
        this.user = user;
        this.favouriteAlbums = favouriteAlbums;
        this.favouriteSongs = favouriteSongs;
        this.favouriteArtists = favouriteArtists;
        this.reviews = reviews;
        this.isOwnProfile = isOwnProfile;
        this.isFollowing = isFollowing;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.reviewCount = reviewCount;
    }
}

