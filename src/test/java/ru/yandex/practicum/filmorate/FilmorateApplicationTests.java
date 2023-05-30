package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserService userService;
    private final FilmService filmService;
    private final GenreService genreService;
    private final MpaService mpaService;
    private User user1;
    private User user2;
    private User user3;
    private Film film1;
    private Film film2;

    @BeforeEach
    public void beforeEach() {
        user1 = User.builder()
                .email("iLikeKit@kit.com")
                .login("Пушистик")
                .name("Ярослав")
                .birthday(LocalDate.of(2001, 9, 2))
                .build();

        user2 = User.builder()
                .email("a4@a4.com")
                .login("jammmissss")
                .name("")
                .birthday(LocalDate.of(2002, 10, 3))
                .build();

        user3 = User.builder()
                .email("don@gmail.com")
                .login("Читер777")
                .name("Тимофей200")
                .birthday(LocalDate.of(2003, 11, 4))
                .build();

        film1 = Film.builder()
                .name("Смешарики")
                .description("Крош решает устроить незабываемый день рождения Копатыча — он обращается в агентство «Дежавю»," +
                        " которое обещает своим клиентам удивительные путешествия во времени.")
                .releaseDate(LocalDate.of(2019, 4, 5))
                .duration(85)
                .build();
        film1.setMpa(new Mpa(1, "G"));
        film1.setGenres(new LinkedHashSet<>(Arrays.asList(new Genre(3, "Мультфильм"),
                new Genre(1, "Комедия"))));

        film2 = Film.builder()
                .name("Ментозавры")
                .description("Проштрафившихся оперативников называют ментозаврами за прошлые заслуги. " +
                        "Сегодня они сосланы в 101-й отдел Коломенского района Санкт-Петербурга как за 101-й километр. ")
                .releaseDate(LocalDate.of(2021, 4, 26))
                .duration(39)
                .build();
        film2.setMpa(new Mpa(2, "PG"));
        film2.setGenres(new LinkedHashSet<>(Arrays.asList(new Genre(6, "Боевик"))));
    }

    @Test
    public void testFindUserById() {
        user1 = userService.create(user1);
        Optional<User> userOptional = Optional.ofNullable(userService.getUserId(user1.getId()));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", user1.getId())
                                .hasFieldOrPropertyWithValue("name", "Ярослав")
                                .hasFieldOrPropertyWithValue("login", "Пушистик")
                );
    }

    @Test
    public void testUpdateUser() {
        user1 = userService.create(user1);
        user1.setName("lin");
        Optional<User> userOptional = Optional.ofNullable(userService.update(user1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "lin")
                );
    }

    @Test
    public void testAddToFriend() {
        user1 = userService.create(user1);
        user2 = userService.create(user2);
        userService.addToFriends(user1.getId(), user2.getId());
        assertThat(userService.getMyFriends(user1.getId()).contains(user2));
    }

    @Test
    public void testDeleteFriend() {
        user1 = userService.create(user1);
        user2 = userService.create(user2);
        userService.addToFriends(user1.getId(), user2.getId());
        assertThat(userService.getMyFriends(user1.getId()).contains(user2));
        userService.deleteFriends(user1.getId(), user2.getId());
        assertThat(userService.getMyFriends(user1.getId()).isEmpty());
    }

    @Test
    public void testGetFriends() {
        user1 = userService.create(user1);
        user2 = userService.create(user2);
        user3 = userService.create(user3);
        userService.addToFriends(user1.getId(), user2.getId());
        userService.addToFriends(user1.getId(), user3.getId());
        assertEquals(userService.getMyFriends(user1.getId()).size(), 2);
    }

    @Test
    public void testMutualFriends() {
        user1 = userService.create(user1);
        user2 = userService.create(user2);
        user3 = userService.create(user3);
        userService.addToFriends(user1.getId(), user2.getId());
        userService.addToFriends(user1.getId(), user3.getId());
        userService.addToFriends(user2.getId(), user1.getId());
        userService.addToFriends(user2.getId(), user3.getId());
        assertThat(userService.mutualFriends(user1.getId(), user2.getId()).contains(user3));
    }

    @Test
    public void testGetFilms() {
        film1 = filmService.create(film1);
        film2 = filmService.create(film2);
        Collection<Film> filmsCollection = filmService.allFilms();
        assertEquals(filmsCollection.size(), 2);
    }

    @Test
    public void testFindFilmById() {
        film1 = filmService.create(film1);
        Optional<Film> filmOptional = Optional.ofNullable(filmService.getFilmId(film1.getId()));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", film.getId())
                                .hasFieldOrPropertyWithValue("name", "Смешарики")
                                .hasFieldOrPropertyWithValue("genres", film.getGenres())
                );
    }

    @Test
    public void testUpdateFilm() {
        film1 = filmService.create(film1);
        film1.setGenres(new LinkedHashSet<>(Arrays.asList(new Genre(6, "Боевик"))));
        Optional<Film> filmOptional = Optional.ofNullable(filmService.update(film1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("genres", film1.getGenres())
                );
    }

    @Test
    public void testPutLike() {
        film1 = filmService.create(film1);
        user3 = userService.create(user3);
        filmService.putLike(film1.getId(), user3.getId());
        Film film = filmService.getFilmId(film1.getId());
        assertThat(film.getLikes().contains(user3.getId()));
    }

    @Test
    public void testDeleteLike() {
        film1 = filmService.create(film1);
        user3 = userService.create(user3);
        filmService.putLike(film1.getId(), user3.getId());
        Film film = filmService.getFilmId(film1.getId());
        assertThat(film.getLikes().contains(user3.getId()));
        filmService.deleteLike(film1.getId(), user3.getId());
        film = filmService.getFilmId(film1.getId());
        assertThat(film.getLikes().isEmpty());
    }

    @Test
    public void testPopularFilm() {
        film1 = filmService.create(film1);
        user1 = userService.create(user1);

        filmService.putLike(film1.getId(), user1.getId());

        List<Film> filmList = filmService.getPopularFilms(3);

        Optional<Film> filmOptional1 = Optional.of(filmList.get(0));
        assertThat(filmOptional1)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", film1.getName())
                );
        filmService.deleteLike(film1.getId(), user1.getId());

        filmList = filmService.getPopularFilms(3);
        assertThat(filmList.isEmpty());
    }

    @Test
    public void testGetAllMpa() {
        Collection<Mpa> mpaCollection = mpaService.getAllMpa();
        assertEquals(mpaCollection.size(), 5);
    }

    @Test
    public void testGetMpaId() {
        Optional<Mpa> mpaOptional = Optional.ofNullable(mpaService.getMpaId(5));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(m ->
                        assertThat(m).hasFieldOrPropertyWithValue("name", "NC-17")
                );
    }

    @Test
    public void testGetAllGenre() {
        Collection<Genre> genreCollection = genreService.getAllGenre();
        assertEquals(genreCollection.size(), 6);
    }

    @Test
    public void testGetGenreId() {
        Optional<Genre> genreOptional = Optional.ofNullable(genreService.getGenreId(2));

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(m ->
                        assertThat(m).hasFieldOrPropertyWithValue("name", "Драма")
                );
    }
}