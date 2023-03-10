package ru.netology.cloudapi.services.impl;

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
import java.util.stream.Collectors;

@Slf4j
@Component
public class FileServiceImpl implements FileService {
    private final FileRepository filesRepository;
    private final UserRepository userRepository;

    public FileServiceImpl(FileRepository filesRepository, UserRepository userRepository) {
        this.filesRepository = filesRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<FileDto> getFileListByUserId(Long id, int limit) {
        List<FileEntity> fileList = filesRepository.findByUserIdAndLimit(id, limit);
        return fileList.stream().map(f -> new FileDto(f.getName(), f.getSize()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public FileEntity upload(String fileName, MultipartFile file, String username) throws IOException {
        if (file.isEmpty()) {
            throw new ErrorData("The file is corrupted or missing", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByUsername(username);

        var fileEntity = FileEntity.builder()
                .size(file.getSize())
                .data(file.getBytes())
                .name(generateFilename(file.getOriginalFilename(), user))
                .created(LocalDate.now())
                .user(user).build();

        log.info("IN upload file : " + file.getOriginalFilename());

        return filesRepository.save(fileEntity);
    }

    @Transactional
    @Override
    public void delete(String username, String filename) {
        User user = userRepository.findByUsername(username);

        if(filesRepository.existsByNameAndUser(filename, user)) {
            log.info("IN delete file WHERE filename = "+filename+" AND user.id = "+user.getId());
            filesRepository.removeByNameAndUser_Id(filename, user.getId());
        } else {
            throw new ErrorData("This file does not exist!", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public FileEntity rename(String filename, FileDto newFile, String userName) {
        User user = userRepository.findByUsername(userName);

        if (user == null) {
            throw new JwtAuthenticationException("User not found!", HttpStatus.UNAUTHORIZED);
        }

        FileEntity fileEntity = filesRepository.findByNameAndUser(filename, user);
        if (fileEntity == null) {
            throw new ErrorData("This file does not exist!", HttpStatus.BAD_REQUEST);
        }

        fileEntity.setName(newFile.getFilename());

        return filesRepository.save(fileEntity);
    }

    @Override
    public FileEntity getFile(String filename, String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new JwtAuthenticationException("User not found!", HttpStatus.UNAUTHORIZED);
        }

        FileEntity fileEntity = filesRepository.findByNameAndUser(filename, user);
        if (fileEntity == null) {
            throw new ErrorData("File not found", HttpStatus.BAD_REQUEST);
        }
        return fileEntity;
    }

    private String generateFilename(String filename, User user) {
        if(filesRepository.existsByNameAndUser(filename, user)) {
            filename = generateFilename("- "+filename, user);
        }

        return filename;
    }
}
