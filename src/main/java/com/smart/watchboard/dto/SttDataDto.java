package com.smart.watchboard.dto;

import com.smart.watchboard.domain.SttData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SttDataDto {
    private List<SttData> data;

    public SttDataDto(List<SttData> data) {
        this.data = data;
    }
}
