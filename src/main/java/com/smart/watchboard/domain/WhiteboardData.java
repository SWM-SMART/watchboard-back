package com.smart.watchboard.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class WhiteboardData {
    private String objId;
    private String type;
    private int x;
    private int y;
    private double depth;
    private String parentId;
    private int w;
    private int h;
    private int fontSize;
    private String overflow;
    private String text;
    private String color;
}
