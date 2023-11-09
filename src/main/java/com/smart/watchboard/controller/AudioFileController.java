package com.smart.watchboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.watchboard.common.support.AwsS3Uploader;
import com.smart.watchboard.domain.SttData;
import com.smart.watchboard.dto.KeywordsDto;
import com.smart.watchboard.dto.S3Dto;
import com.smart.watchboard.dto.SttDto;
import com.smart.watchboard.dto.UploadDto;
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

@RestController
@RequestMapping("/documents")
@Tag(name = "강의 녹음파일 API", description = "강의 녹음파일 관련 API")
@RequiredArgsConstructor
@Slf4j
public class AudioFileController {
    private final AwsS3Uploader awsS3Uploader;
    private final NoteService noteService;
    private final LectureNoteService lectureNoteService;
    private final RequestService requestService;
    private final STTService sttService;
    private final SummaryService summaryService;
    private final FileService fileService;
    private final MindmapService mindmapService;
    private final WhiteboardService whiteboardService;
    private final KeywordService keywordService;

    @PostMapping("/{documentID}/audio")
    public ResponseEntity<?> uploadAudioFile(@PathVariable(value = "documentID") long documentId, @RequestParam("audioFile") MultipartFile audioFile, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        // 토큰 검증
        // s3에 오디오 파일 저장
        S3Dto s3Dto = new S3Dto(audioFile, documentId);
        String path = awsS3Uploader.uploadFile(s3Dto);
        // STT
        //String sttResult = sttService.getSTT(path);
        ResponseEntity<String> sttResponseEntity = sttService.getSTT(path);
        String sttResult = sttService.getText(sttResponseEntity);
        List<SttData> data = sttService.getSTTData(sttResponseEntity);
        lectureNoteService.createLectureNote(documentId, data, sttResult);
        //noteService.createNote(documentId, sttResult);

        ResponseEntity<String> responseEntity = requestService.requestSTTKeywords(sttResult);
        List<String> keywords = keywordService.createKeywords(responseEntity, documentId);

        // 요약본 요청
        String summary = requestService.requestSTTSummary(sttResult);
        summaryService.createSummary(documentId, summary);

        //String body = fileService.createResponseBody(responseEntity, sttResult);
        UploadDto uploadDto = new UploadDto(keywords, data);

        whiteboardService.setDataType(documentId, "audio");

        // 응답 키워드, stt
        return new ResponseEntity<>(uploadDto, HttpStatus.OK);
    }

    @PutMapping("/{documentID}/audio")
    public ResponseEntity<?> updateAudioFile(@PathVariable(value = "documentID") long documentId, @RequestParam("audioFile") MultipartFile audioFile, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        S3Dto s3Dto = new S3Dto(audioFile, documentId);
        String path = awsS3Uploader.uploadFile(s3Dto);

        ResponseEntity<String> sttResponseEntity = sttService.getSTT(path);
        String sttResult = sttService.getText(sttResponseEntity);
        List<SttData> data = sttService.getSTTData(sttResponseEntity);
        lectureNoteService.updateLectureNote(documentId, data, sttResult);

        ResponseEntity<String> responseEntity = requestService.requestSTTKeywords(sttResult);
        List<String> keywords = keywordService.renewKeywords(responseEntity, documentId); // update

        // 요약본 요청
        String summary = requestService.requestSTTSummary(sttResult);
        summaryService.updateSummary(documentId, summary); // update
        UploadDto uploadDto = new UploadDto(keywords, data);

        whiteboardService.setDataType(documentId, "audio");

        return new ResponseEntity<>(uploadDto, HttpStatus.OK);
    }

    @DeleteMapping("/{documentID}/audio")
    public ResponseEntity<?> deleteAudioFile(@PathVariable(value = "documentID") long documentId, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        fileService.deleteAudioFile(documentId);
        // 음성 파일 삭제 했을 때, Stt, 요약본, 마인드맵 모두 삭제 처리
        noteService.deleteNote(documentId);
        summaryService.deleteSummary(documentId);
        // 마인드맵 삭제 구현
        mindmapService.deleteMindmap(documentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{documentID}/audio")
    public ResponseEntity<?> getAudioFile(@PathVariable(value = "documentID") long documentId, @RequestHeader("Authorization") String accessToken) throws JsonProcessingException {
        String path = fileService.getPath(documentId);
        List<SttData> data = lectureNoteService.getData(documentId);
        SttDto body = fileService.createResponseBody(path, data);
        ResponseEntity<?> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/testffff")
    public ResponseEntity<?> test(@RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        String body = """
                {"keywords":["eatssss","food","today"]}
                """;
        //ResponseEntity<String> ss = new ResponseEntity<>(body, HttpStatus.OK);
        List<String> add = new ArrayList<>();
        add.add("awwwww");
        List<String> delete = new ArrayList<>();
        delete.add("eat");
        KeywordsDto keywordsDto = new KeywordsDto(add, delete);
        keywordService.updateKeywords(keywordsDto, 26L);
//        S3Dto s3Dto = new S3Dto(audioFile, 26L);
//        String path = awsS3Uploader.uploadFile(s3Dto);
//        int startIndex = path.indexOf("application/pdf/") + "application/pdf/".length();
//        String extractedString = path.substring(startIndex);
//        System.out.println(extractedString);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
