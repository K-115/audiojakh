package com.makersacademy.audiojakh.service;

import com.makersacademy.audiojakh.model.Track;
import com.makersacademy.audiojakh.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
// 1. CHANGE THIS IMPORT to use full Track model specification

import java.util.ArrayList;
import java.util.List;

@Service
public class SpotifyService {

    private final SpotifyApi spotifyApi;
    private final TrackRepository trackRepository;

    public SpotifyService(
            @Value("${spotify.client.id}") String clientId,
            @Value("${spotify.client.secret}") String clientSecret,
            TrackRepository trackRepository) {

        this.trackRepository = trackRepository;
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
    }

    private void authenticate() {
        try {
            var clientCredentialsRequest = spotifyApi.clientCredentials().build();
            var clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
        } catch (Exception e) {
            throw new RuntimeException("Failed to authenticate with Spotify API", e);
        }
    }

    public List<Track> searchTracks(String query) {
        authenticate();
        List<Track> results = new ArrayList<>();
        try {
            var searchTracksRequest = spotifyApi.searchTracks(query).limit(10).build();
            var trackPaging = searchTracksRequest.execute();

            // 2. FIXED: Use full Track instead of TrackSimplified
            for (se.michaelthelin.spotify.model_objects.specification.Track spotifyTrack : trackPaging.getItems()) {
                Track track = new Track();
                track.setSpotifyId(spotifyTrack.getId());
                track.setName(spotifyTrack.getName());

                if (spotifyTrack.getArtists().length > 0) {
                    track.setArtistId(Long.valueOf(spotifyTrack.getArtists()[0].getId()));
                }

                // 3. FIXED: .getAlbum() can now be called safely!
                if (spotifyTrack.getAlbum() != null) {
                    track.setAlbumId(spotifyTrack.getAlbum().getId());
                }
                results.add(track);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public Track getOrCacheTrack(String spotifyId) {
        return trackRepository.findById(spotifyId).orElseGet(() -> {
            authenticate();
            try {
                var getTrackRequest = spotifyApi.getTrack(spotifyId).build();
                // 4. FIXED: Ensuring this uses the exact full specification item
                se.michaelthelin.spotify.model_objects.specification.Track spotifyTrack = getTrackRequest.execute();

                Track newTrack = new Track();
                newTrack.setSpotifyId(spotifyTrack.getId());
                newTrack.setName(spotifyTrack.getName());

                if (spotifyTrack.getArtists().length > 0) {
                    newTrack.setArtistId(Long.valueOf(spotifyTrack.getArtists()[0].getId()));
                }
                if (spotifyTrack.getAlbum() != null) {
                    newTrack.setAlbumId(spotifyTrack.getAlbum().getId());
                }

                return trackRepository.save(newTrack);
            } catch (Exception e) {
                throw new RuntimeException("Track not found on Spotify: " + spotifyId, e);
            }
        });
    }
}
