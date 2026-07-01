package com.makersacademy.audiojakh.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class SpotifyService {

    private final SpotifyApi spotifyApi;

    public SpotifyService(
            @Value("${spotify.client.id}") String clientId,
            @Value("${spotify.client.secret}") String clientSecret) {
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
        try {
            var searchTracksRequest = spotifyApi.searchTracks(query).limit(10).build();
            return Arrays.asList(searchTracksRequest.execute().getItems());
        } catch (Exception e) {
            throw new RuntimeException("Error searching tracks on Spotify", e);
        }
    }

    public List<Track> searchTracksAdvanced(String query, String year, String genre, String orderBy) {
        authenticate();
        try {
            StringBuilder advancedQuery = new StringBuilder();
            if (query != null && !query.trim().isEmpty()) {
                advancedQuery.append(query.trim());
            }

            if (year != null && !year.trim().isEmpty()) {
                if (advancedQuery.length() > 0) advancedQuery.append(" ");
                advancedQuery.append("year:").append(year.trim());
            }
            if (genre != null && !genre.trim().isEmpty()) {
                if (advancedQuery.length() > 0) advancedQuery.append(" ");
                advancedQuery.append("genre:").append(genre.trim());
            }

            if (advancedQuery.length() == 0) {
                advancedQuery.append("year:2026");
            }

            var searchRequest = spotifyApi.searchTracks(advancedQuery.toString())
                    .build();

            List<Track> tracks = new java.util.ArrayList<>(Arrays.asList(searchRequest.execute().getItems()));

            if ("duration".equalsIgnoreCase(orderBy)) {
                tracks.sort((t1, t2) -> Integer.compare(t2.getDurationMs(), t1.getDurationMs()));
            } else if ("name".equalsIgnoreCase(orderBy)) {
                tracks.sort((t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()));
            }

            return tracks;
        } catch (Exception e) {
            System.err.println("Advanced lookup failed: " + e.getMessage());
            return List.of();
        }
    }




    public List<AlbumSimplified> searchAlbums(String query) {
        authenticate();
        try {
            var searchAlbumsRequest = spotifyApi.searchAlbums(query).limit(10).build();
            return Arrays.asList(searchAlbumsRequest.execute().getItems());
        } catch (Exception e) {
            throw new RuntimeException("Error searching albums on Spotify", e);
        }
    }

    public List<AlbumSimplified> getNewReleases() {
        authenticate();
        try {
            var searchAlbumsRequest = spotifyApi.searchAlbums("year:2026 new releases")
                    .limit(5)
                    .build();
            return Arrays.asList(searchAlbumsRequest.execute().getItems());
        } catch (Exception e) {
            System.err.println("Fallback New Releases Error: " + e.getMessage());
            return List.of();
        }
    }


    public List<Artist> searchArtists(String query) {
        authenticate();
        try {
            var searchArtistsRequest = spotifyApi.searchArtists(query).limit(10).build();
            return Arrays.asList(searchArtistsRequest.execute().getItems());
        } catch (Exception e) {
            throw new RuntimeException("Error searching artists on Spotify", e);
        }
    }

    public Track getTrack(String spotifyId) {
        authenticate();
        try {
            return spotifyApi.getTrack(spotifyId).build().execute();
        } catch (Exception e) {
            throw new RuntimeException("Track not found on Spotify: " + spotifyId, e);
        }
    }

    public Artist getArtist(String artistId) {
        authenticate();
        try {
            return spotifyApi.getArtist(artistId).build().execute();
        } catch (Exception e) {
            throw new RuntimeException("Artist not found on Spotify: " + artistId, e);
        }
    }

    public List<AlbumSimplified> getArtistDiscography(String artistId) {
        authenticate();
        try {
            var getArtistsAlbumsRequest = spotifyApi.getArtistsAlbums(artistId)
                    .build();

            return Arrays.asList(getArtistsAlbumsRequest.execute().getItems());
        } catch (Exception e) {
            System.err.println("Spotify Discography API Error: " + e.getMessage());

            return List.of();
        }
    }





    public Album getAlbum(String albumId) {
        authenticate();
        try {
            return spotifyApi.getAlbum(albumId).build().execute();
        } catch (Exception e) {
            throw new RuntimeException("Album not found on Spotify: " + albumId, e);
        }
    }

    public List<TrackSimplified> getAlbumTracks(String albumId) {
        authenticate();
        try {
            var getAlbumsTracksRequest = spotifyApi.getAlbumsTracks(albumId).build();
            return Arrays.asList(getAlbumsTracksRequest.execute().getItems());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch tracks for album: " + albumId, e);
        }
    }

    public List<Track> getTrendingSongsThisWeek() {
        authenticate();
        try {
            var searchTracksRequest = spotifyApi.searchTracks("Viral Hits 2026")
                    .limit(6)
                    .build();
            return Arrays.asList(searchTracksRequest.execute().getItems());
        } catch (Exception e) {
            System.err.println("Trending Service Error: " + e.getMessage());
            return List.of();
        }
    }

    public List<Track> getMostPopularSongs() {
        authenticate();
        try {
            var searchTracksRequest = spotifyApi.searchTracks("top global hits")
                    .limit(5)
                    .build();
            return Arrays.asList(searchTracksRequest.execute().getItems());
        } catch (Exception e) {
            System.err.println("Fallback Popular Error: " + e.getMessage());
            return List.of();
        }
    }



    public List<AlbumSimplified> getTrendingAlbums() {
        authenticate();
        try {
            var searchAlbumsRequest = spotifyApi.searchAlbums("Top Albums New Releases")
                    .limit(5)
                    .build();

            return Arrays.asList(searchAlbumsRequest.execute().getItems());
        } catch (Exception e) {
            System.err.println("CRITICAL: Spotify Album Service Error -> " + e.getMessage());
            return List.of();
        }
    }
}

