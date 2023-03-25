package ru.netology.cloudapi.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudapi.dto.FileDto;
import ru.netology.cloudapi.exceptions.ErrorData;
import ru.netology.cloudapi.exceptions.JwtAuthenticationException;
import ru.netology.cloudapi.model.FileEntity;
import ru.netology.cloudapi.model.User;
import ru.netology.cloudapi.repositories.FileRepository;
import ru.netology.cloudapi.repositories.UserRepository;
import ru.netology.cloudapi.services.FileService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository filesRepository;
    private final UserRepository userRepository;

    @Override
    public List<FileEntity> getFileListByUserId(Long id, int limit) {
        return filesRepository.findByUserIdAndLimit(id, limit);
    }

    @Transactional
    @Override
    public FileEntity upload(MultipartFile file, String username) throws IOException {
        if (file.isEmpty() && file.getSize() == 0) {
            throw new ErrorData("The filename is corrupted or missing!", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new JwtAuthenticationException("User not found!", HttpStatus.UNAUTHORIZED));

        var fileEntity = FileEntity.builder()
                .size(file.getSize())
                .data(file.getBytes())
                .name(generateFilename(file.getOriginalFilename(), user))
                .created(LocalDate.now())
                .user(user).build();

        log.info("IN upload - file : " + file.getOriginalFilename());

        return filesRepository.save(fileEntity);
    }

    @Transactional
    @Override
    public void delete(String username, String filename) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new JwtAuthenticationException("User not found!", HttpStatus.UNAUTHORIZED));

        if (filesRepository.existsByNameAndUser(filename, user)) {
            log.info("IN delete file WHERE filename = " + filename + " AND user.id = " + user.getId());
            filesRepository.removeByNameAndUser_Id(filename, user.getId());
        } else {
            throw new ErrorData("This file does not exist!", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public FileEntity rename(String filename, FileDto newFile, String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow(() ->
                new JwtAuthenticationException("User not found!", HttpStatus.UNAUTHORIZED));

        FileEntity fileEntity = filesRepository.findByNameAndUser(filename, user).orElseThrow(() ->
                new ErrorData("File not found!", HttpStatus.BAD_REQUEST));

        fileEntity.setName(newFile.getFilename());

        return filesRepository.save(fileEntity);
    }

    @Override
    public FileEntity getFile(String filename, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new JwtAuthenticationException("User not found!", HttpStatus.UNAUTHORIZED));

        return filesRepository.findByNameAndUser(filename, user).orElseThrow(() ->
                new ErrorData("File not found!", HttpStatus.BAD_REQUEST));
    }

    private String generateFilename(String filename, User user) {
        if (filesRepository.existsByNameAndUser(filename, user)) {
            filename = generateFilename("- " + filename, user);
        }

        return filename;
    }
}
