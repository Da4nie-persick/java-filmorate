package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/films")
    public Collection<Film> allFilms() {
        return filmService.allFilms();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void putLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.putLike(id, userId);
    }

    @GetMapping("/films/{id}")
    public Film getFilmId(@PathVariable Integer id) {
        return filmService.getFilmId(id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }
}
