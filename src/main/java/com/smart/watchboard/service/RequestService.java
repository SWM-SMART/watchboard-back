package com.smart.watchboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.watchboard.domain.Summary;
import com.smart.watchboard.dto.KeywordsBodyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {
    @Value("${ai-url}")
    private String aiUrl;

    private final MindmapService mindmapService;
    private final SummaryService summaryService;

    public ResponseEntity<KeywordsBodyDto> requestPdfKeywords(String filePath) {
        int startIndex = filePath.indexOf("application/pdf/") + "application/pdf/".length();
        String fileName = filePath.substring(startIndex);

        RestTemplate restTemplate = new RestTemplate();
        String url = aiUrl + "/keywords";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        // 요청 본문 추가
        String requestBody = """
                {
                  "dbInfo": {
                    "key": "%s",
                    "db": "s3"
                  }
                }
                """.formatted(fileName);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        // 요청 보내기
        try {
            ResponseEntity<KeywordsBodyDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, KeywordsBodyDto.class);
            //ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            System.out.println(responseEntity.getBody());
            //return new ResponseEntity<>(HttpStatus.OK);
            return responseEntity;
        } catch (HttpClientErrorException e) {
            // 에러 응답 처리
            System.err.println("Error response: " + e.getResponseBodyAsString());
            return new ResponseEntity<>(e.getStatusCode());
        }

        // 요청 보내기
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//        System.out.println(responseEntity.getBody().toString());
//        ResponseEntity<String> responseEntity1 = new ResponseEntity<>(HttpStatus.OK);
//        return responseEntity1;
        //        JSONObject requestBody = new JSONObject();
//        requestBody.put("key", "value");
    }

    // 수정필요
    public ResponseEntity<KeywordsBodyDto> requestSTTKeywords(String text) {
        RestTemplate restTemplate = new RestTemplate();
        String url = aiUrl + "/keywords";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 본문 추가
        //String requestBody = "{\"key\": \"" + filePath + "\", \"db\": \"mysql\"}";
        String requestBody = """
                {
                  "dbInfo": {
                    "key": "%s",
                    "db": "mysql"
                  }
                }
                """.formatted(text);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 요청 보내기
        ResponseEntity<KeywordsBodyDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, KeywordsBodyDto.class);
        //ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        return responseEntity;
    }

    public String requestPdfSummary(String filePath) throws JsonProcessingException {
        int startIndex = filePath.indexOf("application/pdf/") + "application/pdf/".length();
        String fileName = filePath.substring(startIndex);

        RestTemplate restTemplate = new RestTemplate();
        String url = aiUrl + "/summary";
        //String url = "http://echo.jsontest.com/key/value/one/two";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 본문 추가
        String requestBody = """
                {
                  "dbInfo": {
                    "key": "%s",
                    "db": "s3"
                  }
                }
                """.formatted(fileName);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        //ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        String text = jsonNode.get("summary").asText();

        return text;
    }

    public String requestSTTSummary(String sttResult) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = aiUrl + "/summary";
        //String url = "http://echo.jsontest.com/key/value/one/two";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 본문 추가
        String requestBody = """
                {
                  "dbInfo": {
                    "key": "%s",
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


    public ResponseEntity<String> requestPdfMindmap(String filePath, Long documentId, List<String> keywords) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = aiUrl + "/mindmap";
        //String url = "http://echo.jsontest.com/key/value/one/two";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 본문 추가
        String requestBody = """
                {
                  "dbInfo": {
                    "key": "%s",
                    "db": "s3"
                  },
                  "keywords": "%s",
                  "documentId": %d
                }
                """.formatted(filePath, keywords.toString(), documentId);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // 마인드맵 mongoDB에 저장
        mindmapService.createMindmap(responseEntity, documentId);

        return responseEntity;
    }

    public ResponseEntity<String> requestSTTMindmap(String stt, Long documentId, List<String> keywords) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = aiUrl + "/mindmap";
        //String url = "http://echo.jsontest.com/key/value/one/two";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 본문 추가
        String requestBody = """
                {
                  "dbInfo": {
                    "key": "%s",
                    "db": "mysql"
                  },
                  "keywords": "%s",
                  "documentId": %d
                }
                """.formatted(stt, keywords.toString(), documentId);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        mindmapService.createMindmap(responseEntity, documentId);

        return responseEntity;
    }

    public ResponseEntity<String> requestAnswer(Long documentId, String keywordLabel) throws JsonProcessingException {
        Summary summary = summaryService.findSummary(documentId);
        RestTemplate restTemplate = new RestTemplate();
        String url = aiUrl + "/question";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestBody = """
                {
                  "summary": "%s",
                  "text": "%s"
                }
                """.formatted(summary.getContent(), keywordLabel);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        return responseEntity;
    }
}
