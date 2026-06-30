package com.makersacademy.audiojakh.repository;


import com.makersacademy.audiojakh.model.FavouriteTrack;
import com.makersacademy.audiojakh.model.Track;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface FavouriteTracksRepository extends CrudRepository<FavouriteTrack, Long> {
    @Query
            (value = "SELECT tracks.* FROM tracks JOIN favourite_tracks ON tracks.spotify_id = favourite_tracks.spotify_id WHERE favourite_tracks.user_id = :userId",
            nativeQuery = true)
    List<Track> findFavouriteTracksByUserId(Long userId);
    @Query("SELECT ft.trackId FROM FavouriteTrack ft WHERE ft.user.id = :userId")
    List<String> findTrackIdsByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM favourite_tracks WHERE user_id = :userId", nativeQuery = true)
    void deleteByUserIdNative(@Param("userId") Long userId);

}
