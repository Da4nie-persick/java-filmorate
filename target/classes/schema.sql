DROP TABLE IF EXISTS genre cascade;
DROP TABLE IF EXISTS mpa cascade;
DROP TABLE IF EXISTS films cascade;
DROP TABLE IF EXISTS users cascade;
DROP TABLE IF EXISTS likes cascade;
DROP TABLE IF EXISTS friends cascade;
DROP TABLE IF EXISTS genre_film cascade;

CREATE TABLE IF NOT EXISTS genre (
     genre_id INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY UNIQUE,
     genre_name VARCHAR NOT NULL UNIQUE);

CREATE TABLE IF NOT EXISTS mpa (
     mpa_id INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     mpa_name VARCHAR NOT NULL);

CREATE TABLE IF NOT EXISTS films (
     film_id INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     name VARCHAR NOT NULL,
     description VARCHAR(200) NOT NULL,
     release_date DATE NOT NULL,
     duration INTEGER NOT NULL,
     mpa_id INTEGER NOT NULL REFERENCES mpa(mpa_id) ON DELETE RESTRICT ON UPDATE CASCADE);

CREATE TABLE IF NOT EXISTS users (
     user_id INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     email VARCHAR NOT NULL,
     login VARCHAR NOT NULL,
     name VARCHAR NOT NULL,
     birthday DATE NOT NULL);

CREATE TABLE IF NOT EXISTS likes (
     user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
     film_id INTEGER NOT NULL REFERENCES films(film_id) ON DELETE RESTRICT ON UPDATE CASCADE);

CREATE TABLE IF NOT EXISTS friends (
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    friend_id INTEGER NOT NULL,
    status boolean);

CREATE TABLE IF NOT EXISTS genre_film (
    film_id INTEGER NOT NULL REFERENCES films(film_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    genre_id INTEGER NOT NULL REFERENCES genre(genre_id) ON DELETE RESTRICT ON UPDATE CASCADE
    );

