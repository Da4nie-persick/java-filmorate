package ru.yandex.practicum.filmorate.storage.additions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class FriendDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addToFriends(Integer id, Integer friendId) {
        String sqlQuery = "insert into friends (user_id, friend_id, status) values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId, true);
    }

    public void deleteFriends(Integer id, Integer friendId) {
        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    public List<User> getFriends(Integer id) {
        List<User> userList = new ArrayList<>();
        String sqlQuery = "select friend_id, email, login, name, birthday from friends " +
                "join users on friends.friend_id = users.user_id where friends.user_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        while (sqlRowSet.next()) {
            userList.add(toUser(sqlRowSet));
        }
        return userList;
    }

    public List<User> mutualFriends(Integer id, Integer otherId) {
        List<User> userList = new ArrayList<>();
        String sqlQuery = "select * from users as u, friends as f1, friends as f2 " +
                "where u.user_id = f1.friend_id and u.user_id = f2.friend_id and f1.user_id = ? and f2.user_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, id, otherId);
        while (sqlRowSet.next()) {
            userList.add(toUser(sqlRowSet));
        }
        return userList;
    }

    private User toUser(SqlRowSet sqlRowSet) {
        return User.builder()
                .id(sqlRowSet.getInt("friend_id"))
                .email(sqlRowSet.getString("email"))
                .login(sqlRowSet.getString("login"))
                .name(sqlRowSet.getString("name"))
                .birthday(sqlRowSet.getDate("birthday").toLocalDate())
                .build();
    }
}

