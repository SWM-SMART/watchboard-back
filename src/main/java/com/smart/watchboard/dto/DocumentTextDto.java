package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DocumentTextDto extends DocumentObjectDto{
    private String objId;
    private String type;
    private int x;
    private int y;
    private double depth;
    private String parentId;
    private int w;
    private int fontSize;
    private String overflow;
    private String text;
    private String color;

    public DocumentTextDto(String objId, String type, int x, int y, double depth, String parentId, int w, int fontSize, String overflow, String text, String color) {
        this.objId = objId;
        this.type = type;
        this.x = x;
        this.y = y;
        this.depth = depth;
        this.parentId = parentId;
        this.w = w;
        this.fontSize = fontSize;
        this.overflow = overflow;
        this.text = text;
        this.color = color;
    }
}
