package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.CommentLikes;
import com.makersacademy.audiojakh.model.Like;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CommentLikeRepository extends CrudRepository <CommentLikes, Long> {
    long countByCommentId(long commentId); // displays count

    Optional<CommentLikes> findByCommentIdAndUserId(Long commentId, Long userID); // toggle implementation

    boolean existsByCommentIdAndUserId(Long commentId, Long userId); // button labelling


}
