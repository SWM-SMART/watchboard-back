package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class DocumentResponseDto {
    private long documentId;
    private String documentName;
    private long createdAt;
    private long modifiedAt;
    private Map<String, DocumentObjectDto> documentData;

    // DocumentData는 map key: value, id : object
}
