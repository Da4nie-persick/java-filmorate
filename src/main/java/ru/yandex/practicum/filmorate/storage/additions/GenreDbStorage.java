package ru.yandex.practicum.filmorate.storage.additions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getAllGenre() {
        String sqlQuery = "select * from genre";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                rs.getString("genre_name")));
    }

    public Genre getGenreId(Integer id) {
        String sqlQuery = "select * from genre where genre_id = ? ";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (sqlRowSet.first()) {
            return new Genre(sqlRowSet.getInt("genre_id"), sqlRowSet.getString("genre_name"));
        }
        throw new ObjectNotFoundException("Жанр не найден");
    }
}