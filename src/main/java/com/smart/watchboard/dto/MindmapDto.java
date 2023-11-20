package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MindmapDto {
    private int root;

    private List<String> keywords;

    private Map<String, List<Integer>> graph;

    public MindmapDto(int root, List<String> keywords, Map<String, List<Integer>> graph) {
        this.root = root;
        this.keywords = keywords;
        this.graph = graph;
    }
}
