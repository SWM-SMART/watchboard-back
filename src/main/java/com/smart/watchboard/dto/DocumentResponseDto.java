package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class DocumentResponseDto {
    private long document_id;
    private String document_name;
    private long created_at;
    private long modified_at;
    private Map<String, DocumentObjectDto> documentData;

    // DocumentData는 map key: value, id : object
}
