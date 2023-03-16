package ru.netology.cloudapi.services.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import ru.netology.cloudapi.enums.Status;
import ru.netology.cloudapi.exceptions.JwtAuthenticationException;
import ru.netology.cloudapi.model.User;
import ru.netology.cloudapi.services.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceImplTest {
    @Autowired
    UserService userService;
    private static final String CONTAINER_NAME_DATABASE = "mysql_db";

    public static GenericContainer<?> app = new GenericContainer("backend:latest")
            .withExposedPorts(8080)
            .withEnv("SPRING_DATASOURCE_URL", "jdbc:mysql://" + CONTAINER_NAME_DATABASE + ":3306/cloudapi")
            .withEnv("SPRING_LIQUIBASE_URL", "jdbc:mysql://" + CONTAINER_NAME_DATABASE + ":3306/cloudapi");

    private User testUser = User.builder()
            .username("test")
            .email("test@example.com")
            .password("password").build();

    @BeforeAll
    public static void setUp() {
        app.start();
    }

    @Test
    public void testRegister() {

        User registeredUser = userService.register(testUser);

        assertNotNull(registeredUser.getId());
        assertEquals(testUser.getUsername(), registeredUser.getUsername());
        assertEquals(testUser.getEmail(), registeredUser.getEmail());
        assertNotNull(registeredUser.getRoles());
        assertEquals(Status.ACTIVE, registeredUser.getStatus());
    }

    @Test
    public void testGetAll() {
        List<User> users = userService.getAll();

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    public void testFindByEmail() {
        User user = userService.findByEmail(testUser.getEmail());

        assertNotNull(user);
        assertEquals(testUser.getEmail(), user.getEmail());
    }

    @Test
    public void testFindByUsername() {
        User user = userService.findByUsername(testUser.getUsername());

        assertNotNull(user);
        assertEquals(testUser.getUsername(), user.getUsername());
    }

    @Test
    public void testFindUserById() {
        User user = userService.findByUsername(testUser.getUsername());

        User result = userService.findUserById(user.getId());

        assertNotNull(user);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    public void testDelete() {
        User user = userService.findByUsername(testUser.getUsername());

        userService.delete(user.getId());

        Exception thrown = Assertions.assertThrows(
                JwtAuthenticationException.class, () -> userService.findUserById(user.getId()));
        Assertions.assertEquals("User not found by id!", thrown.getMessage());
    }
}