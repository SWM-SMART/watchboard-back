package com.smart.watchboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Document("mindmap")
public class Mindmap {
    @Id
    private ObjectId objectId;

    private Long documentId;

    private String documentName;

    private Instant createdAt;

    private Instant modifiedAt;

    private String dataType;

    private int root;

    private List<String> keywords;

    private Map<String, List<Integer>> graph;
}
