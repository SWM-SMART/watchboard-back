package com.smart.watchboard.dto;

import lombok.*;

@Getter
@Setter
@Builder
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

}
