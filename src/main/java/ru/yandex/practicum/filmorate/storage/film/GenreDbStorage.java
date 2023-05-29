package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getAllGenre() {
        String sqlQuery = "select * from genre";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
    }

    public Genre getGenreId(Integer id) {
        String sqlQuery = "select * from genre where genre_id = ? ";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (sqlRowSet.first()) {
            return new Genre(sqlRowSet.getInt("genre_id"), sqlRowSet.getString("genre_name"));
        }
        throw new ObjectNotFoundException("Жанр не найден");
    }

    public void addGenre(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sqlQuery = "insert into genre_film (film_id, genre_id) values (?, ?)";
                jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            }
        }
    }

    public List<Genre> getGenreFilm(Integer id) {
        String sqlQuery = "select * from genre_film as gf " +
                "join genre as g on gf.genre_id = g.genre_id " +
                "where film_id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                rs.getString("genre_name")),id);
    }
}
