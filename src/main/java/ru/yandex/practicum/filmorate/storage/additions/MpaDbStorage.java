package ru.yandex.practicum.filmorate.storage.additions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getAllMpa() {
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
    }

    public Mpa getMpaId(Integer id){
        if(id == null) {
            throw new ObjectNotFoundException("Передан пустой id рейтинга");
        }
        Mpa mpa;
        String sqlQuery = "select * from mpa where mpa_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (sqlRowSet.first()) {
            mpa = new Mpa(sqlRowSet.getInt("mpa_id"), sqlRowSet.getString("mpa_name"));
        } else {
            throw new ObjectNotFoundException("Рейтинг не найден");
        }
        return mpa;
    }
}

