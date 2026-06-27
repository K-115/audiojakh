package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.Album;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumRepository extends CrudRepository<Album, String> {
    List<Album> findTop5ByOrderByReleaseDateDesc();

    @Query(value = "SELECT DISTINCT a.* FROM albums a " +
                    "JOIN tracks t ON t.album_id = a.spotify_id " +
                    "WHERE t.artist_id = :artistId " +
                    "ORDER BY a.release_date DESC NULLS LAST",
                    nativeQuery = true)
    List<Album> findAlbumsByArtistId(@Param("artistId") Long artistId);
}
