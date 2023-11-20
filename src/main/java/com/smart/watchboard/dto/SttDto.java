package com.smart.watchboard.dto;

import com.smart.watchboard.domain.SttData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class SttDto {
    private String url;
    private List<SttData> data;

    public SttDto(String url, List<SttData> data) {
        this.url = url;
        this.data = data;
    }
}
