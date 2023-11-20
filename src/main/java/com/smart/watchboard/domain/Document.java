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

    private Long createdAt;

    private Long modifiedAt;

    private boolean isDeleted;

    private String dataType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToMany(mappedBy = "document")
//    private List<UserDocument> userDocuments = new ArrayList<>();

    @OneToOne(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private File file;

    public Document(String documentName, Long createdAt, Long modifiedAt, boolean isDeleted, String dataType, User user) {
        this.documentName = documentName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isDeleted = isDeleted;
        this.dataType = dataType;
        this.user = user;
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
