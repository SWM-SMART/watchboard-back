package com.smart.watchboard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MindmapResponseDto {
    private int root;
    private List<String> keywords;
    private Map<String, List<Integer>> graph;

    @JsonCreator
    public MindmapResponseDto(@JsonProperty("root") int root, @JsonProperty("keywords") List<String> keywords, @JsonProperty("graph") Map<String, List<Integer>> graph) {
        this.root = root;
        this.keywords = keywords;
        this.graph = graph;
    }
}
