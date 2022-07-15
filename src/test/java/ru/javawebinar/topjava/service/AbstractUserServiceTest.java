package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;

public abstract class AbstractUserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private Environment env;

    @Before
    public void setup() {
        cacheManager.getCache("users").clear();
    }

    @Test
    public void create() {
        User created = service.create(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    public void duplicateMailCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void delete() {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    public void get() {
        User user = service.get(USER_ID);
        USER_MATCHER.assertMatch(user, UserTestData.user);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void getByEmail() {
        User user = service.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    public void update() {
        User updated = getUpdated();
        service.update(updated);
        USER_MATCHER.assertMatch(service.get(USER_ID), getUpdated());
    }

    @Test
    public void getAll() {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, admin, guest, user);
    }

    @Test
    public void createWithExceptionJpa() throws Exception {
        Assume.assumeFalse(Arrays.stream(env.getActiveProfiles()).toList().contains("jdbc"));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "  ", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of())));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of())));
    }
    @Test
    public void createWithExceptionJdbc() throws Exception {
        Assume.assumeTrue(Arrays.stream(env.getActiveProfiles()).toList().contains("jdbc"));
        validateRootCause(ConstraintViolationException.class, () -> validateMeal(new User(null, "  ", "mail@yandex.ru", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> validateMeal(new User(null, "User", "  ", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> validateMeal(new User(null, "User", "mail@yandex.ru", "  ", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> validateMeal(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of())));
        validateRootCause(ConstraintViolationException.class, () -> validateMeal(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of())));
    }

    private void validateMeal(User user) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
    }
}