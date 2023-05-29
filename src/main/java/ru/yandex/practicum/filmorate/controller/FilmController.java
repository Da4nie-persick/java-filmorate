package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import javax.validation.Valid;
import java.util.*;

@RestController
public class FilmController {
    private final FilmService filmService;
    private final MpaService mpaService;
    private final GenreService genreService;

    @Autowired
    public FilmController(FilmService filmService, MpaService mpaService, GenreService genreService) {
        this.filmService = filmService;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    @ResponseBody
    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @ResponseBody
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

    @GetMapping("/mpa")
    public Collection<Mpa> getMpa() {
        return mpaService.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaId(@PathVariable Integer id) {
        return mpaService.getMpaId(id);
    }

    @GetMapping("/genres")
    public Collection<Genre> getGenre() {
        return genreService.getAllGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreId(@PathVariable Integer id) {
        return genreService.getGenreId(id);
    }
}
