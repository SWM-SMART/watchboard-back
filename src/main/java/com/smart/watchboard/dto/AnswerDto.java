package com.smart.watchboard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDto {
    private String text;

    @JsonCreator
    public AnswerDto(@JsonProperty("text") String text) {
        this.text = text;
    }
}
