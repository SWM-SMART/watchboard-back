package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileDto {
    private MultipartFile audioFile;
    private String path;
    private Long documentId;
    private String accessToken;

    public FileDto(MultipartFile multipartFile, String path, Long documentId) {
        this.audioFile = multipartFile;
        this.path = path;
        this.documentId = documentId;
    }
}
