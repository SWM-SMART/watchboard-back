package com.smart.watchboard.dto;

import lombok.*;

@Getter
@Setter
public class DocumentTextDto extends DocumentObjectDto{
    private int w;
    private int fontSize;
    private String overflow;
    private String text;
    private String color;

    public static class DocumentTextDtoBuilder {
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

        public DocumentTextDto.DocumentTextDtoBuilder objId(String objId) {
            this.objId = objId;
            return this;
        }

        public DocumentTextDto.DocumentTextDtoBuilder type(String type) {
            this.type = type;
            return this;
        }

        public DocumentTextDto.DocumentTextDtoBuilder x(int x) {
            this.x = x;
            return this;
        }

        public DocumentTextDto.DocumentTextDtoBuilder y(int y) {
            this.y = y;
            return this;
        }

        public DocumentTextDto.DocumentTextDtoBuilder depth(double depth) {
            this.depth = depth;
            return this;
        }

        public DocumentTextDto.DocumentTextDtoBuilder parentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public DocumentTextDto.DocumentTextDtoBuilder w(int w) {
            this.w = w;
            return this;
        }

        public DocumentTextDto.DocumentTextDtoBuilder fontSize(int fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public DocumentTextDto.DocumentTextDtoBuilder overflow(String overflow) {
            this.overflow = overflow;
            return this;
        }

        public DocumentTextDto.DocumentTextDtoBuilder text(String text) {
            this.text = text;
            return this;
        }

        public DocumentTextDto.DocumentTextDtoBuilder color(String color) {
            this.color = color;
            return this;
        }

        public DocumentTextDto build() {
            DocumentTextDto textDto = new DocumentTextDto();
            textDto.setObjId(objId);
            textDto.setType(type);
            textDto.setX(x);
            textDto.setY(y);
            textDto.setDepth(depth);
            textDto.setParentId(parentId);
            textDto.setW(w);
            textDto.setFontSize(fontSize);
            textDto.setOverflow(overflow);
            textDto.setText(text);
            textDto.setColor(color);

            return textDto;
        }


    }

}
