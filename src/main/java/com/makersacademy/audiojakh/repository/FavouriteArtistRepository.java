package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.FavouriteArtist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteArtistRepository extends CrudRepository<FavouriteArtist, Long> {
    @Query("SELECT fa.artistId FROM FavouriteArtist fa WHERE fa.user.id = :userId")
    List<String> findArtistIdsByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM favourite_artists WHERE user_id = :userId", nativeQuery = true)
    void deleteByUserIdNative(@Param("userId") Long userId);

}
