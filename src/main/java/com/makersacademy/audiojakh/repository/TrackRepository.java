package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.Track;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TrackRepository extends CrudRepository<Track, String> {
    public List<Track> findTracksByAlbumId(String albumId);
}
