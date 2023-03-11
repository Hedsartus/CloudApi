package ru.netology.cloudapi.services;

import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudapi.dto.FileDto;
import ru.netology.cloudapi.model.FileEntity;

import java.io.IOException;
import java.util.List;

public interface FileService {
    List<FileDto> getFileListByUserId(Long id, int Limit);

    FileEntity upload(MultipartFile files, String username) throws IOException;

    void delete(String name, String filename);

    FileEntity rename(String name, FileDto newName, String userName);

    FileEntity getFile(String filename, String username);
}

