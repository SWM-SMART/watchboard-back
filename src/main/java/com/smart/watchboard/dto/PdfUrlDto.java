package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PdfUrlDto {
    public String url;

    public PdfUrlDto(String url) {
        this.url = url;
    }
}
