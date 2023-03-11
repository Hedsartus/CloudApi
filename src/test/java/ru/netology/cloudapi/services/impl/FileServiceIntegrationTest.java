package ru.netology.cloudapi.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.GenericContainer;
import ru.netology.cloudapi.exceptions.CloudApiExceptions;
import ru.netology.cloudapi.exceptions.ErrorData;
import ru.netology.cloudapi.exceptions.JwtAuthenticationException;
import ru.netology.cloudapi.model.FileEntity;
import ru.netology.cloudapi.model.User;
import ru.netology.cloudapi.repositories.UserRepository;

import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileServiceIntegrationTest {
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FileServiceImpl fileService;
    private static final String CONTAINER_NAME_DATABASE = "mysql_db";

    private static final String EMPTY = "_EMPTY_";
    public static GenericContainer<?> app = new GenericContainer("myappp:1.0")
            .withExposedPorts(8080)
            .withEnv("SPRING_DATASOURCE_URL", "jdbc:mysql://"+CONTAINER_NAME_DATABASE+":3306/cloudapi")
            .withEnv("SPRING_LIQUIBASE_URL", "jdbc:mysql://"+CONTAINER_NAME_DATABASE+":3306/cloudapi");

    @BeforeAll
    public static void setUp() {
        app.start();
    }

    @Test
    void getFileTestWhenFileNotFound() {
        Optional<User> user = userRepository.findById(1L);

        if(user.isPresent()) {
            Exception thrown = Assertions.assertThrows(
                    ErrorData.class, () -> fileService.getFile(EMPTY, user.get().getUsername()));
            Assertions.assertEquals("File not found!", thrown.getMessage());
        }
    }

    @Test
    void getFileTestWhenUserNotFound() {
        Exception thrown = Assertions.assertThrows(
                ErrorData.class, () -> fileService.getFile(EMPTY, EMPTY));
        Assertions.assertEquals("User not found!", thrown.getMessage());
    }


}