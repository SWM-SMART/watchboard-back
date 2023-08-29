package com.smart.watchboard.dto;

import com.smart.watchboard.domain.WhiteboardData;

public class DocumentObjectDtoFactory {
    public static DocumentObjectDto createDtoFromWhiteboardData(WhiteboardData whiteboardData) {
        if ("TEXT".equals(whiteboardData.getType())) {
            return new DocumentTextDto.DocumentTextDtoBuilder()
                    .objId(whiteboardData.getObjId())
                    .type(whiteboardData.getType())
                    .x(whiteboardData.getX())
                    .y(whiteboardData.getY())
                    .depth(whiteboardData.getDepth())
                    .parentId(whiteboardData.getParentId())
                    .w(whiteboardData.getW())
                    .fontSize(whiteboardData.getFontSize())
                    .overflow(whiteboardData.getOverflow())
                    .text(whiteboardData.getText())
                    .color(whiteboardData.getColor())
                    .build();

        } else if (whiteboardData.getType().equals("RECT")) {
            return new DocumentRectDto.DocumentRectDtoBuilder()
                    .objId(whiteboardData.getObjId())
                    .type(whiteboardData.getType())
                    .x(whiteboardData.getX())
                    .y(whiteboardData.getY())
                    .depth(whiteboardData.getDepth())
                    .parentId(whiteboardData.getParentId())
                    .w(whiteboardData.getW())
                    .h(whiteboardData.getH())
                    .color(whiteboardData.getColor())
                    .build();

        } else if (whiteboardData.getType().equals("LINE")) {
            return new DocumentLineDto.DocumentLineDtoBuilder()
                    .objId(whiteboardData.getObjId())
                    .type(whiteboardData.getType())
                    .x(whiteboardData.getX())
                    .y(whiteboardData.getY())
                    .depth(whiteboardData.getDepth())
                    .parentId(whiteboardData.getParentId())
                    .w(whiteboardData.getW())
                    .h(whiteboardData.getH())
                    .fontSize(whiteboardData.getFontSize())
                    .color(whiteboardData.getColor())
                    .build();
        }
        return null;
    }
}
