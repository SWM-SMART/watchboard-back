package com.smart.watchboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.DocumentException;
import com.smart.watchboard.common.support.AwsS3Uploader;
import com.smart.watchboard.domain.SttData;
import com.smart.watchboard.dto.*;
import com.smart.watchboard.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static com.smart.watchboard.common.support.PdfConverter.*;


@RestController
@RequestMapping("/documents")
@Tag(name = "강의 녹음파일 API", description = "강의 녹음파일 관련 API")
@RequiredArgsConstructor
@Slf4j
public class AudioFileController {
    private final AwsS3Uploader awsS3Uploader;
    private final NoteService noteService;
    private final LectureNoteService lectureNoteService;
    //private final RequestService requestService;
    private final STTService sttService;
    private final SummaryService summaryService;
    private final FileService fileService;
    private final MindmapService mindmapService;
    private final WhiteboardService whiteboardService;
    private final JwtService jwtService;
    private final SseService sseService;

    @PostMapping("/{documentID}/audio")
    public ResponseEntity<?> uploadAudioFile(@PathVariable(value = "documentID") long documentId, @RequestParam("audio") MultipartFile audioFile, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException, DocumentException {
        String extractedAccessToken = jwtService.extractAccessToken(accessToken);
        Optional<Long> id = jwtService.extractUserId(extractedAccessToken);
        Long userId = id.orElse(null);

        // s3에 오디오 파일 저장
        S3Dto s3Dto = new S3Dto(audioFile, documentId, userId, "mp3");
        String path = awsS3Uploader.uploadFile(s3Dto);
        sseService.notifySTT(documentId, path, userId);

        whiteboardService.setDataType(documentId, "audio");

        // 응답 키워드, stt
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{documentID}/audio")
    public ResponseEntity<?> updateAudioFile(@PathVariable(value = "documentID") long documentId, @RequestParam("audio") MultipartFile audioFile, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException, DocumentException {
        Optional<Long> id = jwtService.extractUserId(accessToken);
        Long userId = id.orElse(null);

        S3Dto s3Dto = new S3Dto(audioFile, documentId, userId, "mp3");
        String path = awsS3Uploader.uploadFile(s3Dto);
        sseService.notifySTT(documentId, path, userId);

        whiteboardService.setDataType(documentId, "audio");

        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<?> test(@RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException, DocumentException {
//        String body = """
//                {"keywords":["eatssss","food","today"]}
//                """;
//        String path = "https://watchboard-record-bucket.s3.ap-northeast-2.amazonaws.com/audio/mp4/26.1.m4a";
//        ResponseEntity<String> responseEntity = sttService.getSTT(path);
//        //System.out.println(responseEntity.getBody().toString());
//        List<SttData> sttData = sttService.getSTTData(responseEntity);
//        String text = sttService.getText(responseEntity);
//        System.out.println(text);
        String sttResult = "안녕하세요 Hello!!!!";
        String sttFileName = "sttResult.pdf";
        File textPdfFile = convertStringToPdf(sttResult, sttFileName);
        String contentType = "application/pdf";
        String originalFilename = textPdfFile.getName();
        String name = textPdfFile.getName();


        FileInputStream fileInputStream = new FileInputStream(textPdfFile);
        MultipartFile multipartFile = new MockMultipartFile(name, originalFilename, contentType, fileInputStream);
        S3Dto s3Dto = new S3Dto(multipartFile, 26L, 7L, "pdf");
        String path = awsS3Uploader.uploadTextPdfFile(s3Dto);
        System.out.println(path);

//        String path = "https://s3.ap-northeast-2.amazonaws.com/watchboard-record-bucket/application/pdf/감정분류.pdf";
//        ResponseEntity<SummaryDto> responseEntity = requestService.requestPdfSummary(path);
//        System.out.println(responseEntity.getBody().getSummary());

//        String sttResult = "안녕하세요";
//        String sttFileName = "hello" + ".txt";
//        File textFile = createTextFile(sttResult, sttFileName);
//        String contentType = "text/plain";
//        String originalFilename = textFile.getName();
//        String name = textFile.getName();
//
//        FileInputStream fileInputStream = new FileInputStream(textFile);
//        MultipartFile multipartFile = createMockMultipartFile(name, originalFilename, textFile);
//        //MultipartFile multipartFile = new MockMultipartFile(name, originalFilename, contentType, fileInputStream);
//        //System.out.println(multipartFile.getInputStream().read());
//        S3Dto s3DtoForSTT = new S3Dto(multipartFile, 26L, 7L, "txt");
//        String textPdfPath = awsS3Uploader.uploadTextPdfFile(s3DtoForSTT);
//        System.out.println(textPdfPath);
//        try (InputStream inputStream = multipartFile.getInputStream()) {
//            // InputStream에서 데이터를 읽어와서 콘솔에 출력
//            int byteRead;
//            while ((byteRead = inputStream.read()) != -1) {
//                System.out.print((char) byteRead);
//            }
//        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
