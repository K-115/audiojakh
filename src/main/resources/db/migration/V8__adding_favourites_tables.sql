CREATE TABLE favourite_albums (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    spotify_id TEXT NOT NULL REFERENCES albums(spotify_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, spotify_id)
);

CREATE TABLE favourite_tracks (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    spotify_id TEXT NOT NULL REFERENCES tracks(spotify_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, spotify_id)
);

CREATE TABLE favourite_artists (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    artist_id BIGINT NOT NULL REFERENCES artists(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, artist_id)
);
