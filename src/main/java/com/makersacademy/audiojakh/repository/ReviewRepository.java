package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Long> {

}


