package ru.netology.cloudapi.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudapi.dto.FileDto;
import ru.netology.cloudapi.model.FileEntity;
import ru.netology.cloudapi.services.FileService;
import ru.netology.cloudapi.services.UserService;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RestController
public class UserRestController {
    private final UserService userService;
    private final FileService fileService;

    @Autowired
    public UserRestController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("/list")
    public Object showAllFiles(@RequestParam("limit") int limit, Principal principal) {
        long userId = userService.findByUsername(principal.getName()).getId();
        log.info("IN showAllFiles > userId = " + userId);
        return fileService.getFileListByUserId(userId, limit);
    }

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public ResponseEntity<?> handleFileUpload(@RequestParam("filename") String filename,
                                              @RequestParam("file") MultipartFile file,
                                              Principal principal) {
        if (!file.isEmpty()) {
            try {
                fileService.upload(filename, file, principal.getName());
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error input data");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error input data");
        }
        return ResponseEntity.status(HttpStatus.OK).body("File uploaded");
    }

    @RequestMapping(value = "/file", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteFile(@RequestParam("filename") String filename, Principal principal) {
        fileService.delete(principal.getName(), filename);
        return ResponseEntity.status(HttpStatus.OK).body("File deleted");
    }

    @RequestMapping(value = "/file", method = RequestMethod.PUT)
    public ResponseEntity<?> updateFileName(@RequestParam("filename") String fileName,
                                            @RequestBody FileDto newName,
                                            Principal principal) {
        fileService.rename(fileName, newName, principal.getName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public ResponseEntity<?> getFile(@RequestParam("filename") String filename, Principal principal) {
        FileEntity file = fileService.getFile(filename, principal.getName());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename='" + file.getName() + "'")
                .body(file.getData());
    }
}
