package com.smart.watchboard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryDto {

    private String summary;

    @JsonCreator
    public SummaryDto(@JsonProperty("summary") String summary) {
        this.summary = summary;
    }
}
