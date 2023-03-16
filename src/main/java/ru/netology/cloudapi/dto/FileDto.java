package ru.netology.cloudapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDto {
    private String filename;
    private Long size;
}