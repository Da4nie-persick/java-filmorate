package ru.yandex.practicum.filmorate.storage.additions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void putLike(Integer filmId, Integer userId) {
        String sqlQuery = "insert into likes (film_id, user_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ? ";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }
}