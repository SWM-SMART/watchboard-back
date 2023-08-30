package com.smart.watchboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Document("whiteboard")
public class Whiteboard {

    @Id
    private ObjectId objectId;

    private Long documentId;

    private String documentName;

    private long createdAt;

    private long modifiedAt;

    private Map<String, WhiteboardData> documentData;
}
