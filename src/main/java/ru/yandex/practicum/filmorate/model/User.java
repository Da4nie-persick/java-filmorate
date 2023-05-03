package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Integer> friends;

    public String getName() {
        if (name == null || name.isBlank()) {
            return login;
        }
        return name;
    }

    public Set<Integer> getFriends() {
        if (friends == null) {
            setFriends(new HashSet<>());
        }
        return friends;
    }
}
