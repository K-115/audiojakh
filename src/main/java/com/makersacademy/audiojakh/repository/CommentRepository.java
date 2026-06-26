package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByReviewIdOrderByDateOfCommentAsc(Long reviewId);
}
