package com.makersacademy.audiojakh.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.makersacademy.audiojakh.model.User;

import java.util.List;

public interface FollowRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT COUNT(*) FROM follows WHERE follower_id = :followerId AND followee_id = :followeeId", nativeQuery = true)
    int isFollowing(@Param("followerId") Long followerId, @Param("followeeId") Long followeeId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO follows (follower_id, followee_id, followed) VALUES (:followerId, :followeeId, true) " +
            "ON CONFLICT (follower_id, followee_id) DO UPDATE SET followed = true", nativeQuery = true)
    void follow(@Param("followerId") Long followerId, @Param("followeeId") Long followeeId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM follows WHERE follower_id = :followerId AND followee_id = :followeeId",
            nativeQuery = true)
    void unfollow(@Param("followerId") Long followerId, @Param("followeeId") Long followeeId);

    @Query(value = "SELECT COUNT(*) FROM follows WHERE followee_id = :userId AND followed = true",
            nativeQuery = true)
    long countFollowersByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT COUNT(*) FROM follows WHERE follower_id = :userId AND followed = true",
            nativeQuery = true)
    long countFollowingByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT u.* FROM users u INNER JOIN  follows f ON u.id = f.follower_id WHERE f.followee_id = :userId AND f.followed = true",
            nativeQuery = true)
    List<User> findFollowersByUserId(@Param("userId") Long userId);

}
