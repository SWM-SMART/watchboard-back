package com.smart.watchboard.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "NOTES")
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Long id;

    private String fileName;
    private String objectKey;

    @Column(columnDefinition = "varchar(1000)", nullable = false)
    private String path;

    private Long size;
    private Instant createdAt;
    private Instant modifiedAt;
    private boolean isDelete;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;
}
