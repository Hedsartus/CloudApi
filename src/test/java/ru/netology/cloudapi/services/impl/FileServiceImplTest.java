package ru.netology.cloudapi.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudapi.exceptions.CloudApiExceptions;
import ru.netology.cloudapi.exceptions.ErrorData;
import ru.netology.cloudapi.exceptions.JwtAuthenticationException;
import ru.netology.cloudapi.model.User;
import ru.netology.cloudapi.repositories.FileRepository;
import ru.netology.cloudapi.repositories.UserRepository;
import ru.netology.cloudapi.services.FileService;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FileServiceImplTest {
    @Mock
    FileRepository filesRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    FileService fileService;

    private static final String EMPTY = "_EMPTY_";

    User user = User.builder().username("user").build();

//    @ParameterizedTest
//    @MethodSource("getArgumentsUpload")
//    void upload(MultipartFile file, String username, CloudApiExceptions exceptions) {
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//
//        Exception thrown = Assertions.assertThrows(
//                ErrorData.class, () -> fileService.upload(null, username));
//
//
//
//    }
//
//
//    public static Stream<Arguments> getArgumentsUpload() {
//        return Stream.of(
//                Arguments.of("", EMPTY, new JwtAuthenticationException("User not found!", HttpStatus.UNAUTHORIZED)),
//                Arguments.of(EMPTY, "user", new ErrorData("The filename is corrupted or missing!", HttpStatus.BAD_REQUEST))
//        );
//    }
}