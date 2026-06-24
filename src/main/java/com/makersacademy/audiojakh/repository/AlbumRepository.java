package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.Album;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepository extends CrudRepository<Album, String> {
}
