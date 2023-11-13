package com.smart.watchboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.watchboard.domain.SttData;
import com.smart.watchboard.dto.SttRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class STTService {
    @Value("${ai-url}")
    private String aiUrl;

    public ResponseEntity<String> getSTT(String path) throws JsonProcessingException {
        String url = aiUrl + "/stt";
        int startIndex = path.indexOf("audio/mp3/") + "audio/mp3/".length();
        String fileName = path.substring(startIndex);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        SttRequestDto sttRequestDto = new SttRequestDto(fileName);
        HttpEntity<SttRequestDto> requestEntity = new HttpEntity<>(sttRequestDto, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        return responseEntity;
    }

    public List<SttData> getSTTData(ResponseEntity<String> sttResponseEntity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(sttResponseEntity.getBody());
        JsonNode segmentsNode = rootNode.get("segments");
        Map<String, List<SttData>> result = new HashMap<>();
        List<SttData> sttDatas = new ArrayList<>();
        for(JsonNode segment : segmentsNode) {
            int start = segment.get("start").asInt();
            int end = segment.get("end").asInt();
            String text = segment.get("text").asText();

            SttData sttData = new SttData(start, end, text);
            sttDatas.add(sttData);
        }

        return sttDatas;
    }

    public String getText(ResponseEntity<String> sttResponseEntity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(sttResponseEntity.getBody());
        String text = rootNode.get("text").asText();

        return text;
    }
}
