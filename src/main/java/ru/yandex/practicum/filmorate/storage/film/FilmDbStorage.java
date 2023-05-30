package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("filmDbStorage")
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private MpaService mpaService;
    private GenreService genreService;
    InMemoryFilmStorage filmController = new InMemoryFilmStorage();

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaService mpaService, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    @Override
    public Film create(Film film) {
        filmController.validate(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(this.toMap(film)).intValue());
        film.setMpa(mpaService.getMpaId(film.getMpa().getId()));
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genre.setName(genreService.getGenreId(genre.getId()).getName());
            }
            genreService.addGenre(film);
        } else {
            film.setGenres(new LinkedHashSet<>());
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ? , duration = ?, mpa_id = ?" +
                "where film_id = ?";
        int updateFilm = jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());
        if (film.getGenres() != null) {
            jdbcTemplate.update("delete from genre_film where film_id = ?", film.getId());
            for (Genre genre : film.getGenres()) {
                genre.setName(genreService.getGenreId(genre.getId()).getName());
            }
            genreService.addGenre(film);
        }
        if (updateFilm == 1)
            return film;
        else
            throw new ObjectNotFoundException("Проверьте данные");
    }

    @Override
    public Collection<Film> allFilms() {
        String sqlQuery = "select * from films ";
        return jdbcTemplate.query(sqlQuery, (this::mapRowToFilms));
    }

    @Override
    public Film getFilmId(Integer id) {
        String sqlQuery = "select * from films where film_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (sqlRowSet.next()) {
            return toFilm(sqlRowSet);
        }
        throw new ObjectNotFoundException("Фильм не найден!");
    }

    public Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", java.sql.Date.valueOf(film.getReleaseDate()));
        values.put("duration", film.getDuration());
        values.put("mpa_id", film.getMpa().getId());
        return values;
    }

    public Film toFilm(SqlRowSet sqlRowSet) {
        int id = sqlRowSet.getInt("film_id");
        return Film.builder()
                .id(sqlRowSet.getInt("film_id"))
                .name(sqlRowSet.getString("name"))
                .description(sqlRowSet.getString("description"))
                .releaseDate(sqlRowSet.getDate("release_date").toLocalDate())
                .duration(sqlRowSet.getInt("duration"))
                .mpa(mpaService.getMpaId(sqlRowSet.getInt("mpa_id")))
                .genres(genreService.getGenreFilm(id))
                .build();
    }

    private Film mapRowToFilms(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("film_id");
        return Film.builder()
                .id(id)
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpaService.getMpaId(resultSet.getInt("mpa_id")))
                .genres(genreService.getGenreFilm(id))
                .build();
    }
}

