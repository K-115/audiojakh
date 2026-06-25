ALTER TABLE reviews ADD COLUMN rating INTEGER CHECK (rating >= 1 AND rating <= 5);
ALTER TABLE reviews ADD COLUMN track_id TEXT REFERENCES tracks(spotify_id);
ALTER TABLE reviews ADD COLUMN album_id TEXT REFERENCES albums(spotify_id);

-- -- Optional: Ensure a review links to at least one or the other
-- ALTER TABLE reviews ADD CONSTRAINT chk_review_target
--     CHECK (track_id IS NOT NULL OR album_id IS NOT NULL);
