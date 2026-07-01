package com.makersacademy.audiojakh.repository;

//import com.makersacademy.audiojakh.DTOs.DTOCommentUserJoin;
import com.makersacademy.audiojakh.model.Comment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByReviewIdOrderByDateOfCommentAsc(Long reviewId);

    Iterable<Comment> findAllByReviewId(Integer reviewId);

    List<Comment> findByReviewId(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.review.id = :reviewId")
    void deleteByReviewId(Long reviewId);



//    @Query(
//            value = "SELECT comments.id, comments.content, comments.post, comments.date_of_comment, users.first_name, users.surname, users.profile_picture FROM comments LEFT JOIN users on comments.poster = users.id WHERE comments.post = :postID;",
//            nativeQuery = true
//    )
//    Iterable<DTOCommentUserJoin> commentsJoin(@Param("reviewId") Integer reviewId);
//
//    @Query(
//            value = "SELECT comments.id, comments.content, comments.post, comments.date_of_comment, users.first_name, users.surname, users.profile_picture FROM comments LEFT JOIN users on comments.poster = users.id",
//            nativeQuery = true
//    )
//    Iterable<DTOCommentUserJoin> commentsJoin();
}
