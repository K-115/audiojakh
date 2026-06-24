package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.Artist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FavouriteArtistRepository extends CrudRepository<Artist, String> {
    @Query
            (value = "SELECT * FROM artists JOIN favourite_artists ON artists.spotify_id = favourite_artists.spotify_id WHERE favourite_artists.user_id = :userId",
                    nativeQuery = true)
    List<Artist> findFavouriteArtistsByUserId(Long userId);
}
