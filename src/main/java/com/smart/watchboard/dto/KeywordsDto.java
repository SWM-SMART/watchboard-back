package com.smart.watchboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KeywordsDto {
    private List<String> add;
    private List<String> delete;

    public KeywordsDto(List<String> add, List<String> delete) {
        this.add = add;
        this.delete = delete;
    }
}
