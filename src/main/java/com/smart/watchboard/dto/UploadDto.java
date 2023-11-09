package com.smart.watchboard.dto;

import com.smart.watchboard.domain.SttData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UploadDto {
    private List<String> keywords;
    private List<SttData> data;

    public UploadDto(List<String> keywords, List<SttData> data) {
        this.keywords = keywords;
        this.data = data;
    }
}
