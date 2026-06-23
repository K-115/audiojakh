package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

}
