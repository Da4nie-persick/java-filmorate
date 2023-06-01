package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.validate.ValidateFilm;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        ValidateFilm.validate(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(this.toMap(film)).intValue());
        addGenre(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ? , duration = ?, mpa_id = ?" +
                "where film_id = ?";
        int updateFilm = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        addGenre(film);
        if (updateFilm == 1) {
            return film;
        } else {
            throw new ObjectNotFoundException("Проверьте данные");
        }
    }

    @Override
    public Collection<Film> allFilms() {
        String sqlQuery = "select * from films left join mpa on films.mpa_id = mpa.mpa_id";
        return uploadingGenres(jdbcTemplate.query(sqlQuery, (this::mapRowToFilms)));
    }

    @Override
    public Optional<Film> getFilmId(Integer id) {
        String sqlQuery = "select * from films join mpa on films.mpa_id = mpa.mpa_id where film_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (sqlRowSet.next()) {
            Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilms, id);
            uploadingGenres(Collections.singletonList(film));
            return Optional.ofNullable(film);
        } else {
            throw new ObjectNotFoundException("Фильм не найден");
        }
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

    public Film mapRowToFilms(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name")))
                .genres(new LinkedHashSet<>())
                .build();
    }

    public List<Film> getPopularFilms(Integer count) {
        String sqlQuery = "select * from films as f " +
                "left join likes as l on f.film_id = l.film_id " +
                "left join mpa as m on f.mpa_id = m.mpa_id group by f.film_id, l.user_id " +
                "order by count(l.user_id) desc limit ?";
        List<Film> filmList = jdbcTemplate.query(sqlQuery, this::mapRowToFilms, count);
        uploadingGenres(filmList);
        return filmList;
    }

    public void addGenre(Film film) {
        if (film.getGenres() != null) {
            jdbcTemplate.update("delete from genre_film where film_id = ?", film.getId());
            List<Genre> genres = new ArrayList<>(film.getGenres());
            jdbcTemplate.batchUpdate("insert into genre_film (film_id, genre_id) values (?, ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    preparedStatement.setInt(1, film.getId());
                    preparedStatement.setInt(2, genres.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return genres.size();
                }
            });
        } else {
            film.setGenres(new LinkedHashSet<>());
        }
    }

    private List<Film> uploadingGenres(List<Film> films) {
        Map<Integer, Film> filmMap = films.stream().collect(Collectors.toMap(Film::getId, f -> f));
        String sql = String.join(", ", Collections.nCopies(films.size(), "?"));
        String sqlQuery = "select * from genre_film as gf " +
                "join genre as g on gf.genre_id = g.genre_id " +
                "where gf.film_id in (" + sql + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            Film film = filmMap.get(rs.getInt("film_id"));
            film.getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
        }, filmMap.keySet().toArray());
        return films;
    }
}

