package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.Artist;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArtistRepository extends CrudRepository<Artist, String> {
    List<Artist> findTop5ByOrderByNameAsc();
}
