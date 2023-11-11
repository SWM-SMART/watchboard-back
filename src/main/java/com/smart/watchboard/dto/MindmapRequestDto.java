package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MindmapRequestDto {
    private String key;
    private String dataType;
    private List<String> keywords;
    private Long documentId;

    public MindmapRequestDto(String key, String dataType, List<String> keywords, Long documentId) {
        this.key = key;
        this.dataType = dataType;
        this.keywords = keywords;
        this.documentId = documentId;
    }
}
