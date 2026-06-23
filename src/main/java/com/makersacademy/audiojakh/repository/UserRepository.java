package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    public Optional<User> findUserByUsername(String username);
}
