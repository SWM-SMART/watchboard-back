package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentObjectDto {
    private String objId;
    private String type;
    private int x;
    private int y;
    private double depth;
    private String parentId;
}
