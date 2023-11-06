package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileDto {
    private MultipartFile file;
    private String path;
    private String fileType;
    private Long documentId;
    //private Long fileId;

    public FileDto(MultipartFile multipartFile, String path, String fileType, Long documentId) {
        this.file = multipartFile;
        this.path = path;
        this.fileType = fileType;
        this.documentId = documentId;
        //this.fileId = fileId;
    }
}
