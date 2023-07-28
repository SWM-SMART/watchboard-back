package com.smart.watchboard.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
@Getter
public class Document {
    private long documentId;
    private String documentName;
//    private Timestamp createdAt;
//    private Timestamp modifiedAt;
    private long createdAt;
    private long modifiedAt;

    public Document(long documentId, String documentName, long createdAt, long modifiedAt) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "Document{" +
                "documentId=" + documentId +
                ", documentName='" + documentName + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
