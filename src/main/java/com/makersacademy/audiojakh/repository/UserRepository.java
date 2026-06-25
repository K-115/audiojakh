package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmailAddress(String emailAddress);
    List<User> findAllByOrderByUsernameAsc();

    @Query(
            value = """
                SELECT *
                FROM users
                WHERE username ILIKE CONCAT('%', :search, '%')
                    OR firstName ILIKE CONCAT('%', :search, '%')
                    OR surname ILIKE CONCAT('%', :search, '%')
            """,
            nativeQuery = true
    )
    List<User> searchUsers(@Param("search") String search);
}
