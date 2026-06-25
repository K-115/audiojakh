package com.makersacademy.audiojakh.repository;


import com.makersacademy.audiojakh.model.Song;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FavouriteTracksRepository extends CrudRepository<Song, String> {
    @Query
            (value = "SELECT tracks.* FROM tracks JOIN favourite_tracks ON tracks.spotify_id = favourite_tracks.spotify_id WHERE favourite_tracks.user_id = :userId",
            nativeQuery = true)
    List<Song> findFavouriteTracksByUserId(Long userId);

}
