package com.smart.watchboard.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@AllArgsConstructor
@Getter
@Document("whiteboard")
public class Whiteboard {

    @Field("_id")
    private ObjectId objectId;

    private Long documentId;

    private String documentName;

    private long createdAt;

    private long modifiedAt;

    private Map<String, WhiteboardData> documentData;
}
