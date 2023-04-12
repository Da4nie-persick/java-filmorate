package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    User user;
    Validator validator;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .email("dasha141099@gmail.com")
                .login("Da4nie_persick")
                .name("Dasha")
                .birthday(LocalDate.of(1999, 10, 14)).build();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void checkingInvalidEmail() {
        user.setEmail("dasha141099.gmail.com");
        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        assertFalse(violationSet.isEmpty());
        assertEquals(violationSet.iterator().next().getMessage(), "должно иметь формат адреса электронной почты");
    }

    @Test
    public void checkingNotLogin() {
        user.setLogin("");
        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        assertFalse(violationSet.isEmpty());
        assertEquals(violationSet.iterator().next().getMessage(), "не должно быть пустым");
    }

    @Test
    public void checkingNotName() {
        user.setName(null);
        assertEquals(user.getName(), "Da4nie_persick");
    }

    @Test
    public void checkingInvalidBirthday() {
        user.setBirthday(LocalDate.of(2028, 10, 14));
        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        assertFalse(violationSet.isEmpty());
        assertEquals(violationSet.iterator().next().getMessage(), "должно содержать прошедшую дату");
    }

    @Test
    public void checkingAllRight() {
        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        assertTrue(violationSet.isEmpty());
    }
}

