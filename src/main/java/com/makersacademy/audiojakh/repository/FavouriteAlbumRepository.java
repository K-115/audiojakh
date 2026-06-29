package com.makersacademy.audiojakh.repository;
import com.makersacademy.audiojakh.model.Album;
import com.makersacademy.audiojakh.model.FavouriteAlbum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavouriteAlbumRepository extends CrudRepository<FavouriteAlbum, Long> {
        @Query
                (value = "SELECT a.* FROM albums a JOIN favourite_albums f ON a.spotify_id = f.spotify_id WHERE f.user_id = :userId",
                nativeQuery = true)
        List<Album> findFavouriteAlbumsByUserId(Long userId);
    @Query("SELECT fa.albumId FROM FavouriteAlbum fa WHERE fa.user.id = :userId")
    List<String> findAlbumIdsByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM favourite_albums WHERE user_id = :userId", nativeQuery = true)
    void deleteByUserIdNative(@Param("userId") Long userId);

}


