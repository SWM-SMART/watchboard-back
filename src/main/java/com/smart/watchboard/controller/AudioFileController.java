package com.smart.watchboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.watchboard.common.support.AwsS3Uploader;
import com.smart.watchboard.domain.File;
import com.smart.watchboard.domain.SttData;
import com.smart.watchboard.dto.S3Dto;
import com.smart.watchboard.dto.SttDto;
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

    @PostMapping("/{documentID}/audio")
    public ResponseEntity<?> uploadAudioFile(@PathVariable(value = "documentID") long documentId, @RequestParam("audioFile") MultipartFile audioFile, @RequestParam(value = "fileID", required = false) Long fileId, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        // 토큰 검증
        // s3에 오디오 파일 저장
        S3Dto s3Dto = new S3Dto(audioFile, documentId, fileId);
        String path = awsS3Uploader.uploadFile(s3Dto);
        // STT
        //String sttResult = sttService.getSTT(path);
        ResponseEntity<String> sttResponseEntity = sttService.getSTT(path);
        String sttResult = sttService.getText(sttResponseEntity);
        List<SttData> data = sttService.getSTTData(sttResponseEntity);
        lectureNoteService.createLectureNote(documentId, data);
        //noteService.createNote(documentId, sttResult);

        // STT 키워드 요청
        ResponseEntity<String> responseEntity = requestService.requestSTTKeywords(sttResult);

        // 요약본 요청
        String summary = requestService.requestSTTSummary(sttResult);
        summaryService.createSummary(documentId, summary);

        //String body = fileService.createResponseBody(responseEntity, sttResult);
        SttDto body = fileService.createResponseBody(path, data);

        // 응답 키워드, stt
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PutMapping("/{documentID}/audio")
    public ResponseEntity<?> updateAudioFile(@PathVariable(value = "documentID") long documentId, @RequestParam("audioFile") MultipartFile audioFile, @RequestParam(value = "fileID", required = false) Long fileId, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        S3Dto s3Dto = new S3Dto(audioFile, documentId, fileId);
        String path = awsS3Uploader.uploadFile(s3Dto);

        ResponseEntity<String> sttResponseEntity = sttService.getSTT(path);
        String sttResult = sttService.getText(sttResponseEntity);
        List<SttData> data = sttService.getSTTData(sttResponseEntity);

        //String sttResult = sttService.getSTT(path);
//        noteService.updateNote(documentId, sttResult);
//
//        ResponseEntity<String> responseEntity = requestService.requestSTTKeywords(sttResult);
//        String summary = requestService.requestSTTSummary(sttResult);
//        summaryService.updateSummary(documentId, summary);
//
//        String body = fileService.createResponseBody(responseEntity, sttResult);

        return new ResponseEntity<>("", HttpStatus.OK);
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
                {
                    "segments":[
                        {
                            "start":0,
                            "end":7827,
                            "text":"알겠습니다. 나지 할게 나는 뭐 바꿨냐면 어제는 api 요청 방식 수정을",
                            "confidence":0.8284265,
                            "diarization":{
                            "label":"1"
                            },
                            "speaker":{},
                            "words":[],
                            "textEdited":"알겠습니다. 나지 할게 나는 뭐 바꿨냐면 어제는 api 요청 방식 수정을"
                        },
                        {
                            "start":7827,
                            "end":12412,
                            "text":"이게 뭔 얘기냐면 아까 얘기했듯이",
                            "confidence":0.92000407,
                            "diarization":{
                                "label":"2"
                            },
                            "speaker":{
                                "label":"2",
                                "name":"B",
                                "edited":false
                            },
                            "words":[],
                            "textEdited":"이게 뭔 얘기냐면 아까 얘기했듯이"
                        }
                    ]
                }
                """;
        ResponseEntity<String> response1 = new ResponseEntity<>(body, HttpStatus.OK);
        List<SttData> data = sttService.getSTTData(response1);
        System.out.println(data.get(0).getText());
        lectureNoteService.createLectureNote(100L, data);
        String path = "naver.com";
        SttDto body2 = fileService.createResponseBody(path, data);
        ResponseEntity<?> ss = new ResponseEntity<>(body2, HttpStatus.OK);
        return ss;
    }

}
