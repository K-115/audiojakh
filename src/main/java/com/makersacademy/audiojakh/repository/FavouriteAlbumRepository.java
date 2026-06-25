package com.makersacademy.audiojakh.repository;
import com.makersacademy.audiojakh.model.Album;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface FavouriteAlbumRepository extends CrudRepository<Album, String> {
        @Query
                (value = "SELECT a.* FROM albums a JOIN favourite_albums f ON a.spotify_id = f.spotify_id WHERE f.user_id = :userId",
                nativeQuery = true)
        List<Album> findFavouriteAlbumsByUserId(Long userId);
    }


