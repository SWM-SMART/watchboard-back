package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class DocumentCreatedResponseDto {
    private long documentId;
    private String documentName;
    private long createdAt;
    private long modifiedAt;

    public DocumentCreatedResponseDto(long documentId, String documentName, long createdAt, long modifiedAt) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
