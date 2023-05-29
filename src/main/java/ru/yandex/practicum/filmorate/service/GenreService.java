package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreDbStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Collection<Genre> getAllGenre() {
        return genreDbStorage.getAllGenre().stream().sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toList());
    }

    public Genre getGenreId(Integer id) {
        return genreDbStorage.getGenreId(id);
    }

    public void addGenre(Film film) {
        genreDbStorage.addGenre(film);
    }

    public LinkedHashSet<Genre> getGenreFilm(Integer id) {
        return new LinkedHashSet<>(genreDbStorage.getGenreFilm(id));
    }
}
