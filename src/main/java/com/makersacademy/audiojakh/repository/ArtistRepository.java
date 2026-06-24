package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.Artist;
import org.springframework.data.repository.CrudRepository;

public interface ArtistRepository extends CrudRepository<Artist, Long> {
}
