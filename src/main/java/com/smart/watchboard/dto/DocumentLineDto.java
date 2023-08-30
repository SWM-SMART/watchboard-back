package com.smart.watchboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentLineDto extends DocumentObjectDto {
    private int w;
    private int h;
    private int fontSize;
    private String color;

    public static class DocumentLineDtoBuilder {
        private String objId;
        private String type;
        private int x;

        private int y;
        private double depth;
        private String parentId;
        private int w;
        private int h;
        private int fontSize;
        private String color;

        public DocumentLineDto.DocumentLineDtoBuilder objId(String objId) {
            this.objId = objId;
            return this;
        }

        public DocumentLineDto.DocumentLineDtoBuilder type(String type) {
            this.type = type;
            return this;
        }

        public DocumentLineDto.DocumentLineDtoBuilder x(int x) {
            this.x = x;
            return this;
        }

        public DocumentLineDto.DocumentLineDtoBuilder y(int y) {
            this.y = y;
            return this;
        }

        public DocumentLineDto.DocumentLineDtoBuilder depth(double depth) {
            this.depth = depth;
            return this;
        }

        public DocumentLineDto.DocumentLineDtoBuilder parentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public DocumentLineDto.DocumentLineDtoBuilder w(int w) {
            this.w = w;
            return this;
        }

        public DocumentLineDto.DocumentLineDtoBuilder h(int h) {
            this.h = h;
            return this;
        }

        public DocumentLineDto.DocumentLineDtoBuilder fontSize(int fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public DocumentLineDto.DocumentLineDtoBuilder color(String color) {
            this.color = color;
            return this;
        }

        public DocumentLineDto build() {
            DocumentLineDto lineDto = new DocumentLineDto();
            lineDto.setObjId(objId);
            lineDto.setType(type);
            lineDto.setX(x);
            lineDto.setY(y);
            lineDto.setDepth(depth);
            lineDto.setParentId(parentId);
            lineDto.setW(w);
            lineDto.setH(h);
            lineDto.setFontSize(fontSize);
            lineDto.setColor(color);

            return lineDto;
        }


    }
}
