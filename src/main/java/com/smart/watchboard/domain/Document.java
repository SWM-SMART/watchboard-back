package com.smart.watchboard.domain;

import com.smart.watchboard.dto.DocumentDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "DOCUMENT")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private long documentId;

    private String documentName;

    private Instant createdAt;

    private Instant modifiedAt;

    private boolean isDeleted;

    @OneToMany(mappedBy = "document")
    private List<UserDocument> userDocuments = new ArrayList<>();

    @OneToOne(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private File file;

    public Document(String documentName, Instant createdAt, Instant modifiedAt, boolean isDeleted) {
        this.documentName = documentName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isDeleted = isDeleted;
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
