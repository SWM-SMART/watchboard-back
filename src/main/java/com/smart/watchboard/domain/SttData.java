package com.smart.watchboard.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class SttData {
    int start;
    int end;
    String text;

    public SttData(int start, int end, String text) {
        this.start = start;
        this.end = end;
        this.text = text;
    }
}
