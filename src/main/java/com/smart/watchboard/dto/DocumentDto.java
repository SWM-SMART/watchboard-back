package com.smart.watchboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {

    private Long documentId;
    private String documentName;
    private long createdAt;
    private long modifiedAt;
}
