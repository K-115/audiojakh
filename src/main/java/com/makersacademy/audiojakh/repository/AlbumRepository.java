package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.Album;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AlbumRepository extends CrudRepository<Album, String> {
    List<Album> findTop5ByOrderByReleaseDateDesc();

}
