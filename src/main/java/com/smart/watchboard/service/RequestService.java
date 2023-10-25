package com.smart.watchboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.watchboard.repository.FileRepository;
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
public class RequestService {
    private final FileRepository fileRepository;

    public ResponseEntity<String> requestPdfKeywords(String filePath) {
        RestTemplate restTemplate = new RestTemplate();
        //String url = "https:/{aiurl}/keywords";
        String url = "http://echo.jsontest.com/key/value/one/two";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 본문 추가
        String requestBody = """
                {
                  "dbInfo": {
                    "key": %s,
                    "db": "s3"
                  }
                }
                """.formatted(filePath);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        return responseEntity;
    }

    // 수정필요
    public ResponseEntity<String> requestSTTKeywords(String text) {
        RestTemplate restTemplate = new RestTemplate();
        //String url = "https:/{aiurl}/keywords";
        String url = "http://echo.jsontest.com/key/value/one/two";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 본문 추가
        //String requestBody = "{\"key\": \"" + filePath + "\", \"db\": \"mysql\"}";
        String requestBody = """
                {
                  "dbInfo": {
                    "key": %s,
                    "db": "mysql"
                  }
                }
                """.formatted(text);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        //ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        return responseEntity;
    }

    public ResponseEntity<String> requestPdfSummary(String filePath) {
        RestTemplate restTemplate = new RestTemplate();
        //String url = "https:/{aiurl}/mindmap";
        String url = "http://echo.jsontest.com/key/value/one/two";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 본문 추가
        String requestBody = """
                {
                  "dbInfo": {
                    "key": %s,
                    "db": "s3"
                  }
                }
                """.formatted(filePath);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        //ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        return responseEntity;
    }

    public String requestSTTSummary(String sttResult) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        //String url = "https:/{aiurl}/mindmap";
        String url = "http://echo.jsontest.com/key/value/one/two";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 본문 추가
        String requestBody = """
                {
                  "dbInfo": {
                    "key": %s,
                    "db": "mysql"
                  }
                }
                """.formatted(sttResult);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        //ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        String text = jsonNode.get("summary").asText();

        //return responseEntity;
        return text;
    }


    public ResponseEntity<String> requestPdfMindmap(String filePath) {
        RestTemplate restTemplate = new RestTemplate();
        //String url = "https:/{aiurl}/mindmap";
        String url = "http://echo.jsontest.com/key/value/one/two";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 본문 추가
        String requestBody = "{\"key\": \"" + filePath + "\", \"db\": \"s3\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 요청 보내기
        //ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        // 마인드맵 mongoDB에 저장

        return responseEntity;
    }


}
