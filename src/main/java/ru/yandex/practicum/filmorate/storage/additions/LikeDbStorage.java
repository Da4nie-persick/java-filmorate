package ru.yandex.practicum.filmorate.storage.additions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.*;

@Component
public class LikeDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private MpaService mpaService;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate, MpaService mpaService){
        this.jdbcTemplate = jdbcTemplate;
        this.mpaService = mpaService;
    }

    public void putLike(Integer filmId, Integer userId) {
        String sqlQuery = "insert into likes (film_id, user_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ? ";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        List<Film> filmList = new ArrayList<>();
        String sqlQuery = "select * from films as f " +
                "left join likes as l on f.film_id = l.film_id group by f.film_id, l.user_id " +
                "order by count(l.user_id) desc limit ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, count);
        while (sqlRowSet.next()) {
            filmList.add(toFilm(sqlRowSet));
        }
        return filmList;
    }

    public List<Integer> getLikes(Integer id) {
        String sqlQuery = "select user_id from likes where film_id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("user_id"), id);
    }

    public Film toFilm(SqlRowSet sqlRowSet) {
        return Film.builder()
                .id(sqlRowSet.getInt("film_id"))
                .name(sqlRowSet.getString("name"))
                .description(sqlRowSet.getString("description"))
                .releaseDate(sqlRowSet.getDate("release_date").toLocalDate())
                .duration(sqlRowSet.getInt("duration"))
                .mpa(mpaService.getMpaId(sqlRowSet.getInt("mpa_id")))
                .genres(new LinkedHashSet<>())
                .build();
    }
}

