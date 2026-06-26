package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.ReviewLike;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends CrudRepository<ReviewLike, Long> {
    long countByReviewId(Long reviewId);
    Optional<ReviewLike> findByReviewIdAndUserId(Long reviewId, Long userId);
    boolean existsByReviewIdAndUserId(Long reviewId, Long userId);
}
