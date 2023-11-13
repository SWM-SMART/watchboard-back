package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SttRequestDto {
    private String key;

    public SttRequestDto(String key) {
        this.key = key;
    }
}
