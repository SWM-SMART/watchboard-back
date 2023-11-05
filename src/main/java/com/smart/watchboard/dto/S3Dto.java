package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@RequiredArgsConstructor
public class S3Dto {
    private MultipartFile file;
    private Long documentId;
    //private Long fileId;

    public S3Dto(MultipartFile file, long documentId) {
        this.file = file;
        this.documentId = documentId;
        //this.fileId = fileId;
    }
}
