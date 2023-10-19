package com.smart.watchboard.controller;

import com.smart.watchboard.common.support.AudioConcatenator;
import com.smart.watchboard.common.support.AwsS3Uploader;
import com.smart.watchboard.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@RestController
@RequestMapping("/graph")
@Tag(name = "그래프 API", description = "그래프 관련 API(mock)")
@RequiredArgsConstructor
@Slf4j
public class GraphController {
    
    private final AwsS3Uploader awsS3Uploader;
    private final AudioConcatenator audioConcatenator;
    private final String aiServerUrl = "";

    @PostMapping("/{documentID}")
    @Operation(summary = "마인드맵 생성", description = "음성 데이터를 받아 ai 서버에 마인드맵 요청한다.")
    public ResponseEntity<?> getMindmap(@PathVariable(value = "documentID") long documentId, @RequestParam("audioFile") MultipartFile audioFile, @RequestParam(value = "fileID", required = false) Long fileId, @RequestHeader("Authorization") String accessToken) {
        //awsS3Uploader.uploadFile(audioFile, documentId, fileId);
        //awsS3Uploader.deleteFile();

        return new ResponseEntity<>("{\"root\":1,\"keywords\":[\"나는\",\"eat\",\"food\",\"today\"],\"graph\":{\"1\":[0,2],\"2\":[3]}}", HttpStatus.OK);
    }

    @PostMapping("/abc")
    public ResponseEntity<?> test(@RequestParam("audioFile1") MultipartFile audioFile1, @RequestParam(value = "fileID", required = false) Long fileId, @RequestParam(value = "documentID") Long documentId) throws UnsupportedAudioFileException, IOException {
        audioConcatenator.concatenateAudioFiles(audioFile1, documentId, fileId);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
