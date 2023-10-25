package com.smart.watchboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class STTService {

    private final String secretKey = "dc5790a7fda04de18cbf1e1ed670de54";
    private final String clovaInvokeURL = "https://clovaspeech-gw.ncloud.com/external/v1/6162/970e15ec7ee55dd9f17886f9d538146ae7dfa8a5c19e25c15e6e3626b74da491";

    public String getSTT(String path) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String requestURL = clovaInvokeURL + "/recognizer/url";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-CLOVASPEECH-API-KEY", secretKey);
        String requestBody = "{" +
                "\"url\": \"" + path + "\", " +
                "\"language\": \"ko-KR\", " +
                "\"completion\": \"sync\"" +
                "}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        System.out.println(requestEntity.getBody());
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.POST, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());

        String text = jsonNode.get("text").asText();
        System.out.println(text);

        return text;
    }
}
