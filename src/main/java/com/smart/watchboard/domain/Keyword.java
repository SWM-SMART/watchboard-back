package com.smart.watchboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Document("keyword")
public class Keyword {
    @Id
    private ObjectId objectId;

    private Long documentId;

    private List<String> keywords;
}
