package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@RequiredArgsConstructor
public class DocumentRectDto extends DocumentObjectDto {
    private String objId;
    private String type;
    private int x;
    private int y;
    private double depth;
    private String parentId;
    private int w;
    private int h;
    private String color;

    public DocumentRectDto(String objId, String type, int x, int y, double depth, String parentId, int w, int h, String color) {
        this.objId = objId;
        this.type = type;
        this.x = x;
        this.y = y;
        this.depth = depth;
        this.parentId = parentId;
        this.w = w;
        this.h = h;
        this.color = color;
    }



}
