package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class DocumentCreatedResponseDto {
    private long document_id;
    private String document_name;
    private long created_at;
    private long modified_at;

    public DocumentCreatedResponseDto(long documentId, String documentName, long createdAt, long modifiedAt) {
        this.document_id = documentId;
        this.document_name = documentName;
        this.created_at = createdAt;
        this.modified_at = modifiedAt;
    }
}
