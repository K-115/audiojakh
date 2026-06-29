package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    boolean existsByUserIdAndTrackSpotifyId(Long userId, String spotifyId);
    boolean existsByUserIdAndAlbumSpotifyId(Long userId, String spotifyId);
    @Query(
            value= "SELECT * FROM reviews WHERE date_of_review >= NOW() - INTERVAL '7 days' ORDER BY likes DESC LIMIT 5",
            nativeQuery = true)
    List<Review> findTop5TrendingReviewsThisWeek();
    List<Review> findByUserIdOrderByDateOfReviewDesc(Long userId);

    @Query(
            value = "SELECT COUNT(*) FROM reviews WHERE user_id = :userId", nativeQuery = true)
    long countReviewsByUserId(@Param("userId") Long userId);
    List<Review> findAllByOrderByIdDesc();

    List<Review> findAllByOrderByLikesDesc();

}



