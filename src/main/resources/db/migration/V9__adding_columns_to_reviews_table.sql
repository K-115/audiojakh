ALTER TABLE reviews ADD COLUMN rating INTEGER CHECK (rating >= 1 AND rating <= 5);
ALTER TABLE reviews ADD COLUMN track_id TEXT REFERENCES tracks(spotify_id);
ALTER TABLE reviews ADD COLUMN album_id TEXT REFERENCES albums(spotify_id);

CREATE UNIQUE INDEX uq_user_track ON reviews (user_id, track_id) WHERE track_id IS NOT NULL;
CREATE UNIQUE INDEX uq_user_album ON reviews (user_id, album_id) WHERE album_id IS NOT NULL;

