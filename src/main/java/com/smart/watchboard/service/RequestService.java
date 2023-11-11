package com.smart.watchboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.watchboard.domain.Summary;
import com.smart.watchboard.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
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

//        // 요청 본문 추가
//        String requestBody = """
//                {
//                  "dbInfo": {
//                    "key": "%s",
//                    "dataType": "pdf"
//                  }
//                }
//                """.formatted(fileName);
        RequestBodyToServerDto requestBodyToServerDto = new RequestBodyToServerDto(fileName, "audio");
        HttpEntity<RequestBodyToServerDto> requestEntity = new HttpEntity<>(requestBodyToServerDto, headers);
        // 요청 보내기
        try {
            ResponseEntity<KeywordsBodyDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, KeywordsBodyDto.class);
            //ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
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
    public ResponseEntity<KeywordsBodyDto> requestSTTKeywords(String filePath) {
        int startIndex = filePath.indexOf("application/pdf/") + "application/pdf/".length();
        String fileName = filePath.substring(startIndex);

        RestTemplate restTemplate = new RestTemplate();
        String url = aiUrl + "/keywords";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        RequestBodyToServerDto requestBodyToServerDto = new RequestBodyToServerDto(fileName, "pdf");
        HttpEntity<RequestBodyToServerDto> requestEntity = new HttpEntity<>(requestBodyToServerDto, headers);

        // 요청 보내기
        ResponseEntity<KeywordsBodyDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, KeywordsBodyDto.class);
        //ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        return responseEntity;
    }

    public ResponseEntity<SummaryDto> requestPdfSummary(String filePath) throws JsonProcessingException, UnsupportedEncodingException {
        int startIndex = filePath.indexOf("application/pdf/") + "application/pdf/".length();
        String fileName = filePath.substring(startIndex);
        System.out.println(fileName);

        RestTemplate restTemplate = new RestTemplate();
        String url = aiUrl + "/summary";
        //String url = "http://echo.jsontest.com/key/value/one/two";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

//        // 요청 본문 추가
//        String requestBody = """
//                {
//                  "key": "%s",
//                  "dataType": "pdf"
//                }
//                """.formatted(fileName);

        RequestBodyToServerDto requestBodyToServerDto = new RequestBodyToServerDto(fileName, "pdf");
        HttpEntity<RequestBodyToServerDto> requestEntity = new HttpEntity<>(requestBodyToServerDto, headers);

        // 요청 보내기
        ResponseEntity<SummaryDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, SummaryDto.class);
        //ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
//        responseEntity.getBody().
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
//        String text = jsonNode.get("summary").asText();

        return responseEntity;
    }

        public ResponseEntity<SummaryDto> requestSTTSummary(String filePath) throws JsonProcessingException {
        int startIndex = filePath.indexOf("application/pdf/") + "application/pdf/".length();
        String fileName = filePath.substring(startIndex);

        RestTemplate restTemplate = new RestTemplate();
        String url = aiUrl + "/summary";
        //String url = "http://echo.jsontest.com/key/value/one/two";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        RequestBodyToServerDto requestBodyToServerDto = new RequestBodyToServerDto(fileName, "audio");
        HttpEntity<RequestBodyToServerDto> requestEntity = new HttpEntity<>(requestBodyToServerDto, headers);

        // 요청 보내기
        ResponseEntity<SummaryDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, SummaryDto.class);
        //ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
//        String text = jsonNode.get("summary").asText();

        //return responseEntity;
        return responseEntity;
    }


    public ResponseEntity<MindmapResponseDto> requestPdfMindmap(String filePath, Long documentId, List<String> keywords) throws JsonProcessingException {
        int startIndex = filePath.indexOf("application/pdf/") + "application/pdf/".length();
        String fileName = filePath.substring(startIndex);

        RestTemplate restTemplate = new RestTemplate();
        String url = aiUrl + "/mindmap";
        //String url = "http://echo.jsontest.com/key/value/one/two";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

//        // 요청 본문 추가
//        String requestBody = """
//                {
//                  "dbInfo": {
//                    "key": "%s",
//                    "dataType": "pdf"
//                  },
//                  "keywords": "%s",
//                  "documentId": %d
//                }
//                """.formatted(filePath, keywords.toString(), documentId);
//        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        MindmapRequestDto mindmapRequestDto = new MindmapRequestDto(fileName, "pdf", keywords, documentId);
        HttpEntity<MindmapRequestDto> requestEntity = new HttpEntity<>(mindmapRequestDto, headers);

        // 요청 보내기
        ResponseEntity<MindmapResponseDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, MindmapResponseDto.class);

        // 마인드맵 mongoDB에 저장
        mindmapService.createMindmap(responseEntity, documentId);

        return responseEntity;
    }

    public ResponseEntity<MindmapResponseDto> requestSTTMindmap(String filePath, Long documentId, List<String> keywords) throws JsonProcessingException {
        int startIndex = filePath.indexOf("application/pdf/") + "application/pdf/".length();
        String fileName = filePath.substring(startIndex);

        RestTemplate restTemplate = new RestTemplate();
        String url = aiUrl + "/mindmap";
        //String url = "http://echo.jsontest.com/key/value/one/two";
        // 요청 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        MindmapRequestDto mindmapRequestDto = new MindmapRequestDto(fileName, "pdf", keywords, documentId);
        HttpEntity<MindmapRequestDto> requestEntity = new HttpEntity<>(mindmapRequestDto, headers);

        // 요청 보내기
        ResponseEntity<MindmapResponseDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, MindmapResponseDto.class);

        mindmapService.createMindmap(responseEntity, documentId);

        return responseEntity;
    }

    public ResponseEntity<AnswerDto> requestAnswer(Long documentId, String keywordLabel) throws JsonProcessingException {
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

        ResponseEntity<AnswerDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AnswerDto.class);

        return responseEntity;
    }
}
