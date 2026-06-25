package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    boolean existsByUserIdAndTrackSpotifyId(Long userId, String spotifyId);
    boolean existsByUserIdAndAlbumSpotifyId(Long userId, String spotifyId);
}



