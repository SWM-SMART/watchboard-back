package com.smart.watchboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.watchboard.common.support.AwsS3Uploader;
import com.smart.watchboard.domain.File;
import com.smart.watchboard.dto.*;
import com.smart.watchboard.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/documents")
@Tag(name = "pdf 학습파일 API", description = "학습파일 관련 API")
@RequiredArgsConstructor
@Slf4j
public class LearningFileController {
    private final AwsS3Uploader awsS3Uploader;
    private final FileService fileService;
    private final RequestService requestService;
    private final WhiteboardService whiteboardService;
    private final SummaryService summaryService;
    private final KeywordService keywordService;
    private final JwtService jwtService;

    @PostMapping("/{documentID}/pdf")
    public ResponseEntity<?> uploadLearningFile(@PathVariable(value = "documentID") long documentId, @RequestParam("pdf") MultipartFile pdfFile, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        Optional<Long> id = jwtService.extractUserId(accessToken);
        Long userId = id.orElse(null);

        S3Dto s3Dto = new S3Dto(pdfFile, documentId, userId, "pdf");
        String path = awsS3Uploader.uploadFile(s3Dto);
        ResponseEntity<KeywordsBodyDto> responseEntity = requestService.requestPdfKeywords(path);
        keywordService.createKeywords(responseEntity, documentId);

        ResponseEntity<SummaryDto> summary = requestService.requestPdfSummary(path);
        summaryService.createSummary(documentId, summary.getBody().getSummary());
        whiteboardService.setDataType(documentId, "pdf");

        return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
    }

    @PutMapping("/{documentID}/pdf")
    public ResponseEntity<?> updateLearningFile(@PathVariable(value = "documentID") long documentId, @RequestParam("pdf") MultipartFile pdfFile, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        Optional<Long> id = jwtService.extractUserId(accessToken);
        Long userId = id.orElse(null);

        S3Dto s3Dto = new S3Dto(pdfFile, documentId, userId, "pdf");
        String path = awsS3Uploader.uploadFile(s3Dto);
        ResponseEntity<KeywordsBodyDto> responseEntity = requestService.requestPdfKeywords(path);
        keywordService.renewKeywords(responseEntity, documentId);

        ResponseEntity<SummaryDto> summary = requestService.requestPdfSummary(path);
        summaryService.updateSummary(documentId, summary.getBody().getSummary());
        whiteboardService.setDataType(documentId, "pdf");

        return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
    }

    @DeleteMapping("/{documentID}/pdf")
    public ResponseEntity<?> deleteLearningFile(@PathVariable(value = "documentID") long documentId, @RequestParam(value = "fileID", required = false) Long fileId, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        fileService.deleteFile(fileId);

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/{documentID}/pdf")
    public ResponseEntity<?> getLearningFile(@PathVariable(value = "documentID") long documentId, @RequestHeader("Authorization") String accessToken) {
        String body = fileService.getPdfUrl(documentId);
        ResponseEntity<?> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestHeader("Authorization") String accessToken, @RequestParam("pdf") MultipartFile pdfFile) throws JsonProcessingException {
        Optional<Long> id = jwtService.extractUserId(accessToken);
        Long userId = id.orElse(null);

        S3Dto s3Dto = new S3Dto(pdfFile, 11L, userId, "pdf");
        String path = awsS3Uploader.uploadFile(s3Dto);
        //System.out.println(path);
        //String path = "abcd";
        //ResponseEntity<String> responseEntity = requestService.requestPdfKeywords(path);
//        String xx = """
//                {"root":1,"keywords":["나는","eat","food","today"],"graph":{"1":[0,2],"2":[3]}}
//                """;

        String xx = """
                {
                	"add": ["추가할", "키워드", "목록"],
                	"delete": ["eat"],
                }
                """;
//        List<String> add = new ArrayList<>();
//        add.add("추가할");
//        add.add("키워드");
//        List<String> delete = new ArrayList<>();
//        delete.add("eat");
//        KeywordsDto keywordsDto = new KeywordsDto(add, delete);
        //ResponseEntity<String> entity = new ResponseEntity<>(xx, HttpStatus.OK);
//        mindmapService.updateKeywords(keywordsDto, 11L);
        //mindmapService.createMindmap(entity, 11L, "pdf");
        //String body = fileService.getPdfUrl(4L);
        //System.out.println(body);

        return new ResponseEntity<>("", HttpStatus.OK);

    }
}
