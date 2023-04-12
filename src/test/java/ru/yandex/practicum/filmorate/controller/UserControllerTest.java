package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
        assertEquals(violationSet.iterator().next().getMessage(), "must be a well-formed email address");
    }

    @Test
    public void checkingNotLogin() {
        user.setLogin("");
        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        assertFalse(violationSet.isEmpty());
        assertEquals(violationSet.iterator().next().getMessage(), "must not be blank");
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
        assertEquals(violationSet.iterator().next().getMessage(), "must be a past date");
    }

    @Test
    public void checkingAllRight() {
        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        assertTrue(violationSet.isEmpty());
    }
}

