package com.smart.watchboard.dto;

import lombok.*;

@Getter
@Setter
public class DocumentRectDto extends DocumentObjectDto {
    private int w;
    private int h;
    private String color;

    public static class DocumentRectDtoBuilder {
        private String objId;
        private String type;
        private int x;
        private int y;
        private double depth;
        private String parentId;
        private int w;
        private int h;
        private String color;

        public DocumentRectDto.DocumentRectDtoBuilder objId(String objId) {
            this.objId = objId;
            return this;
        }

        public DocumentRectDto.DocumentRectDtoBuilder type(String type) {
            this.type = type;
            return this;
        }

        public DocumentRectDto.DocumentRectDtoBuilder x(int x) {
            this.x = x;
            return this;
        }

        public DocumentRectDto.DocumentRectDtoBuilder y(int y) {
            this.y = y;
            return this;
        }

        public DocumentRectDto.DocumentRectDtoBuilder depth(double depth) {
            this.depth = depth;
            return this;
        }

        public DocumentRectDto.DocumentRectDtoBuilder parentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public DocumentRectDto.DocumentRectDtoBuilder w(int w) {
            this.w = w;
            return this;
        }

        public DocumentRectDto.DocumentRectDtoBuilder h(int h) {
            this.h = h;
            return this;
        }

        public DocumentRectDto.DocumentRectDtoBuilder color(String color) {
            this.color = color;
            return this;
        }

        public DocumentRectDto build() {
            DocumentRectDto rectDto = new DocumentRectDto();
            rectDto.setObjId(objId);
            rectDto.setType(type);
            rectDto.setX(x);
            rectDto.setY(y);
            rectDto.setDepth(depth);
            rectDto.setParentId(parentId);
            rectDto.setW(w);
            rectDto.setH(h);
            rectDto.setColor(color);

            return rectDto;
        }
    }

}
