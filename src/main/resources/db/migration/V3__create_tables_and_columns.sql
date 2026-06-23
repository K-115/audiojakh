DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS commentLikes;
DROP TABLE IF EXISTS follows;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS reviewLikes;
DROP TABLE IF EXISTS albums;
DROP TABLE IF EXISTS artists;
DROP TABLE IF EXISTS tracks;


CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100),
    profile_picture TEXT,
    first_name VARCHAR(100),
    surname VARCHAR(100),
    email_address VARCHAR(100),
    followers TEXT,
    dob DATE
);

CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    content TEXT,
    user_id BIGINT NOT NULL REFERENCES users(id),
    likes INTEGER,
    date_of_review TIMESTAMP DEFAULT NOW()
);

CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    content TEXT,
    poster BIGINT NOT NULL REFERENCES users(id),
    review BIGINT NOT NULL REFERENCES reviews(id),
    likes BIGINT,
    date_of_comment TIMESTAMP DEFAULT NOW(),
    UNIQUE(poster, review)
);


CREATE TABLE commentLikes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    comment_id BIGINT NOT NULL REFERENCES comments(id),
    CONSTRAINT uq_likes_user_comment UNIQUE (user_id, comment_id)
);


CREATE TABLE follows (
    id BIGSERIAL PRIMARY KEY,
    follower_id BIGINT NOT NULL REFERENCES users(id),
    followee_id BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP DEFAULT NOW(),
    followed BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE(follower_id, followee_id)
);



CREATE TABLE reviewsLikes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    review_id BIGINT NOT NULL REFERENCES reviews(id),
    CONSTRAINT uq_likes_user_review UNIQUE (user_id, review_id)
);

CREATE TABLE albums (
    spotify_id TEXT PRIMARY KEY, -- track_album_id from the CSV
    name TEXT NOT NULL,
    release_date DATE,
    album_cover TEXT NOT NULL
);

CREATE TABLE artists (
    id BIGSERIAL PRIMARY KEY, -- placeholder key, CSV does not provide artist key
    name TEXT NOT NULL UNIQUE,
    artist_image TEXT NOT NULL
);


CREATE TABLE tracks (
    spotify_id TEXT PRIMARY KEY, -- track_id from the CSV
    name TEXT NOT NULL,
    album_id TEXT REFERENCES albums(spotify_id),
    duration_ms INTEGER, -- stores raw ms, can be formatted at display time separately
    genre TEXT
);
