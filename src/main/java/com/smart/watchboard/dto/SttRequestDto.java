package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SttRequestDto {
    private String audioUrl;

    public SttRequestDto(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}
