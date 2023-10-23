package com.smart.watchboard.controller;

import com.smart.watchboard.common.support.AwsS3Uploader;
import com.smart.watchboard.dto.FileDto;
import com.smart.watchboard.dto.S3Dto;
import com.smart.watchboard.service.FileService;
import com.smart.watchboard.service.RequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@RestController
@RequestMapping("/documents")
@Tag(name = "pdf 학습파일 API", description = "학습파일 관련 API")
@RequiredArgsConstructor
@Slf4j
public class LearningFileController {
    private final AwsS3Uploader awsS3Uploader;
    private final FileService fileService;
    private final RequestService requestService;

    @PostMapping("/{documentID}/pdf")
    public ResponseEntity<?> uploadLearningFile(@PathVariable(value = "documentID") long documentId, @RequestParam("pdfFile") MultipartFile pdfFile, @RequestParam(value = "fileID", required = false) Long fileId, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        S3Dto s3Dto = new S3Dto(pdfFile, documentId, fileId);
        String path = awsS3Uploader.uploadFile(s3Dto);
        ResponseEntity<String> responseEntity = requestService.requestPdfKeywords(path);

        return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
    }

    @PutMapping("/{documentID}/pdf")
    public ResponseEntity<?> updateLearningFile(@PathVariable(value = "documentID") long documentId, @RequestParam("pdfFile") MultipartFile pdfFile, @RequestParam(value = "fileID", required = false) Long fileId, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        S3Dto s3Dto = new S3Dto(pdfFile, documentId, fileId);
        String path = awsS3Uploader.uploadFile(s3Dto);
        ResponseEntity<String> responseEntity = requestService.requestPdfKeywords(path);

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @DeleteMapping("/{documentID}/pdf")
    public ResponseEntity<?> deleteLearningFile(@PathVariable(value = "documentID") long documentId, @RequestParam(value = "fileID", required = false) Long fileId, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        fileService.deleteFile(fileId);

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        String path = "abcd";
        ResponseEntity<String> responseEntity = requestService.requestPdfKeywords(path);

        return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);

    }
}
