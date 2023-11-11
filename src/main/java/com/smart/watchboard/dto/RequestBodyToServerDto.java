package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestBodyToServerDto {
    private String key;
    private String dataType;

    public RequestBodyToServerDto(String key, String dataType) {
        this.key = key;
        this.dataType = dataType;
    }
}
