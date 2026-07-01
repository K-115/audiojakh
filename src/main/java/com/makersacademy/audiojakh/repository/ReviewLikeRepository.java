package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.ReviewLike;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ReviewLikeRepository extends CrudRepository<ReviewLike, Long> {
    long countByReviewId(Long reviewId);
    Optional<ReviewLike> findByReviewIdAndUserId(Long reviewId, Long userId);
    boolean existsByReviewIdAndUserId(Long reviewId, Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ReviewLike rl WHERE rl.reviewId = :reviewId")
    void deleteByReviewId(Long reviewId);

}
