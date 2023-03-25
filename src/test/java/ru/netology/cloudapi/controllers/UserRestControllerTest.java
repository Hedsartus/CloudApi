package ru.netology.cloudapi.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.netology.cloudapi.dto.FileDto;
import ru.netology.cloudapi.model.FileEntity;
import ru.netology.cloudapi.model.User;
import ru.netology.cloudapi.services.FileService;
import ru.netology.cloudapi.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRestControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private FileService fileService;
    @InjectMocks
    private UserRestController userRestController;

    @Test
    public void testShowAllFiles() {
        Principal principal = new UsernamePasswordAuthenticationToken("user", "password");
        User user = new User();
        user.setId(1L);
        when(userService.findByUsername(principal.getName())).thenReturn(user);

        List<FileEntity> fileList = new ArrayList<>();
        FileEntity file1 = FileEntity.builder().name("file1.txt").size(12123L).build();
        fileList.add(file1);

        FileEntity file2 = FileEntity.builder().name("file2.txt").size(1123L).build();
        fileList.add(file2);

        when(fileService.getFileListByUserId(user.getId(), 10)).thenReturn(fileList);

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(userRestController.showAllFiles(10, principal));
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(fileList, responseEntity.getBody());
    }
}