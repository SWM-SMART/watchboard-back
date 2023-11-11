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
    private Long userId;
    private String dataType;

    public S3Dto(MultipartFile file, Long documentId, Long userId, String dataType) {
        this.file = file;
        this.documentId = documentId;
        this.userId = userId;
        this.dataType = dataType;
    }
}
