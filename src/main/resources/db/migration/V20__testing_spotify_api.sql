ALTER TABLE tracks DROP CONSTRAINT IF EXISTS tracks_artist_id_fkey;
ALTER TABLE tracks DROP CONSTRAINT IF EXISTS tracks_album_id_fkey;
ALTER TABLE favourite_artists DROP CONSTRAINT IF EXISTS favourite_artists_artist_id_fkey;

DELETE FROM favourite_artists;

ALTER TABLE favourite_artists
ALTER COLUMN artist_id TYPE VARCHAR(255) USING artist_id::varchar;

ALTER TABLE tracks
ALTER COLUMN artist_id TYPE VARCHAR(255) USING artist_id::varchar;

ALTER TABLE tracks
ALTER COLUMN album_id TYPE VARCHAR(255) USING album_id::varchar;
ALTER TABLE tracks ADD artist_name VARCHAR(255);

ALTER TABLE artists
ALTER COLUMN id TYPE VARCHAR(255) using id::varchar;

ALTER TABLE artists RENAME COLUMN name to artist_name;

ALTER TABLE favourite_artists DROP CONSTRAINT IF EXISTS favourite_artists_pkey;

ALTER TABLE favourite_artists ADD COLUMN id BIGSERIAL;

ALTER TABLE favourite_artists ADD CONSTRAINT favourite_artists_id_pkey PRIMARY KEY (id);

ALTER TABLE favourite_albums DROP CONSTRAINT IF EXISTS favourite_albums_pkey;

ALTER TABLE favourite_albums ADD COLUMN id BIGSERIAL;

ALTER TABLE favourite_albums ADD CONSTRAINT favourite_albums_id_pkey PRIMARY KEY (id);

ALTER TABLE favourite_tracks DROP CONSTRAINT IF EXISTS favourite_tracks_pkey;

ALTER TABLE favourite_tracks ADD COLUMN id BIGSERIAL;

ALTER TABLE favourite_tracks ADD CONSTRAINT favourite_tracks_id_pkey PRIMARY KEY (id);


ALTER TABLE reviews ADD COLUMN IF NOT EXISTS track_name VARCHAR(255);
ALTER TABLE reviews ADD COLUMN IF NOT EXISTS album_name VARCHAR(255);

ALTER TABLE reviews ADD COLUMN IF NOT EXISTS track_spotify_id VARCHAR(255);
ALTER TABLE reviews ADD COLUMN IF NOT EXISTS album_spotify_id VARCHAR(255);

ALTER TABLE reviews ADD COLUMN IF NOT EXISTS artist_name VARCHAR(255);

ALTER TABLE favourite_tracks ADD CONSTRAINT unique_user_favourite_track UNIQUE (user_id);
ALTER TABLE favourite_albums ADD CONSTRAINT unique_user_favourite_album UNIQUE (user_id);
ALTER TABLE favourite_artists ADD CONSTRAINT unique_user_favourite_artist UNIQUE (user_id);
ALTER TABLE favourite_tracks DROP CONSTRAINT IF EXISTS favourite_tracks_spotify_id_fkey;
ALTER TABLE favourite_albums DROP CONSTRAINT IF EXISTS favourite_albums_spotify_id_fkey;
ALTER TABLE reviews ADD COLUMN IF NOT EXISTS image_url VARCHAR(500);
DELETE FROM commentLikes;
DELETE FROM comments;
ALTER TABLE comments ADD COLUMN IF NOT EXISTS user_id BIGINT NOT NULL;
ALTER TABLE comments
DROP CONSTRAINT IF EXISTS fk_comments_user,
ADD CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
DELETE FROM commentLikes;
DELETE FROM comments;
ALTER TABLE comments DROP COLUMN IF EXISTS poster;
ALTER TABLE comments ADD COLUMN IF NOT EXISTS user_id BIGINT;
UPDATE comments SET user_id = 1 WHERE user_id IS NULL;
ALTER TABLE comments ALTER COLUMN user_id SET NOT NULL;
ALTER TABLE comments
DROP CONSTRAINT IF EXISTS fk_comments_user,
ADD CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

