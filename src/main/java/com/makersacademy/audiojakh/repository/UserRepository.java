package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmailAddress(String emailAddress);
    List<User> findAllByOrderByUsernameAsc();
}
