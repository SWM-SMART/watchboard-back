package com.smart.watchboard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KeywordsBodyDto {
    private List<String> keywords;

    @JsonCreator
    public KeywordsBodyDto(@JsonProperty("keywords") List<String> keywords) {
        this.keywords = keywords;
    }
}
