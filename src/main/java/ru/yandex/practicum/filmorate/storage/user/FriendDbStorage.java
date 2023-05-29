package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class FriendDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Autowired
    public FriendDbStorage(@Qualifier("userDbStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    public void addToFriends(Integer id, Integer friendId) {
        userStorage.getUserId(id);
        User friend = userStorage.getUserId(friendId);
        boolean status = false;
        if (friend.getFriends().contains(id)) {
            status = true;
            String sqlQuery = "update friends set user_id = ? and friend_id = ? and status = ? where user_id = ? and friend_id = ? ";
            jdbcTemplate.update(sqlQuery, id, friendId, true);
        }
        String sqlQuery = "insert into friends (user_id, friend_id, status) values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId, status);
    }

    public void deleteFriends(Integer id, Integer friendId) {
        userStorage.getUserId(id);
        User friend = userStorage.getUserId(friendId);
        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
        if (friend.getFriends().contains(id)) {
            boolean status = false;
            sqlQuery = "update friends set user_id = ? and friend_id = ? and status = ? where user_id = ? and friend_id = ? ";
            jdbcTemplate.update(sqlQuery, id, friendId, status);
        }
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
