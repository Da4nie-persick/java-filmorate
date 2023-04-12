package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    Film film;
    Validator validator;

    @BeforeEach
    public void setUp() {
        film = Film.builder()
                .name("Голодные игры")
                .description("Будущее. Деспотичное государство ежегодно устраивает показательные игры на выживание, за которыми " +
                        "в прямом эфире следит весь мир. Жребий участвовать в Играх выпадает юной Китнисс и тайно влюбленному " +
                        "в нее Питу. Они знакомы с детства, но теперь должны стать врагами. Ведь по нерушимому закону Голодных " +
                        "игр победить может только один из 24 участников. Судьям не важно, кто выиграет, главное — зрелище. " +
                        "И на этот раз зрелище будет незабываемым.")
                .releaseDate(LocalDate.of(2012, Month.MARCH, 22))
                .duration(142).build();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void checking200SizeDescription() {
        Set<ConstraintViolation<Film>> violationSet = validator.validate(film);
        assertFalse(violationSet.isEmpty());
        assertEquals(violationSet.iterator().next().getMessage(), "размер должен находиться в диапазоне от 0 до 200");
    }

    @Test
    public void checkingNotName() {
        film.setName(null);
        film.setDescription("Фильм");
        Set<ConstraintViolation<Film>> violationSet = validator.validate(film);
        assertFalse(violationSet.isEmpty());
        assertEquals(violationSet.iterator().next().getMessage(), "не должно равняться null");
    }

    @Test
    public void checkingNotDate() {
        film.setReleaseDate(null);
        film.setDescription("Фильм");
        Set<ConstraintViolation<Film>> violationSet = validator.validate(film);
        assertFalse(violationSet.isEmpty());
        assertEquals(violationSet.iterator().next().getMessage(), "не должно равняться null");
    }

    @Test
    public void checkingNegativeDuration() {
        film.setDuration(-8);
        film.setDescription("Фильм");
        Set<ConstraintViolation<Film>> violationSet = validator.validate(film);
        assertFalse(violationSet.isEmpty());
        assertEquals(violationSet.iterator().next().getMessage(), "должно быть больше 0");
    }

    @Test
    public void checkingAllRight() {
        film.setDescription("Фильм");
        Set<ConstraintViolation<Film>> violationSet = validator.validate(film);
        assertTrue(violationSet.isEmpty());
    }
}
