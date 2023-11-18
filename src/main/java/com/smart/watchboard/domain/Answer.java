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
@Document("answer")
public class Answer {
    @Id
    private ObjectId objectId;

    private Long documentId;

    private String keyword;

    private String text;
}
