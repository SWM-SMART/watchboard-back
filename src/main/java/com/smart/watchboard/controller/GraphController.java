package com.smart.watchboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.Keyword;
import com.smart.watchboard.dto.AnswerDto;
import com.smart.watchboard.dto.KeywordsBodyDto;
import com.smart.watchboard.dto.KeywordsDto;
import com.smart.watchboard.dto.MindmapDto;
import com.smart.watchboard.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "그래프 API", description = "그래프 관련 API")
@RequiredArgsConstructor
@Slf4j
public class GraphController {
    
    private final LectureNoteService lectureNoteService;
    private final MindmapService mindmapService;
    //private final RequestService requestService;
    private final FileService fileService;
    private final WhiteboardService whiteboardService;
    private final SseService sseService;
    private final KeywordService keywordService;
    private final JwtService jwtService;
    @PostMapping("/graph/{documentID}")
    @Operation(summary = "마인드맵 생성", description = "ai 서버에 마인드맵 요청한다.")
    public void createMindmap(@PathVariable(value = "documentID") long documentId, @RequestBody KeywordsBodyDto keywordsBodyDto, @RequestHeader("Authorization") String accessToken) throws JsonProcessingException {
        sseService.notify(documentId, keywordsBodyDto.getKeywords());
//        if (whiteboardService.isPdfType(documentId)) {
//            String path = fileService.getPdfUrl(documentId);
//            ResponseEntity<String> body = requestService.requestPdfMindmap(path, documentId, keywords);
//            return new ResponseEntity<>(body, HttpStatus.OK);
//        } else if (whiteboardService.isAudioType(documentId)) {
//            String text = lectureNoteService.getText(documentId);
//            ResponseEntity<String> body = requestService.requestSTTMindmap(text, documentId, keywords);
//            return new ResponseEntity<>(body, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/documents/{documentID}/mindmap")
    @Operation(summary = "마인드맵 조회", description = "마인드맵 조회")
    public ResponseEntity<?> getMindmap(@PathVariable(value = "documentID") long documentId, @RequestHeader("Authorization") String accessToken) {
        Optional<Long> id = jwtService.extractUserId(accessToken);
        Long userId = id.orElse(null);

        MindmapDto mindmapDto = mindmapService.getMindmap(documentId);
        if (mindmapDto != null) {
            return new ResponseEntity<>(mindmapDto, HttpStatus.OK);
        }

        Document document = whiteboardService.findDoc(documentId);
        if (document.getDataType().equals("pdf")) {
            String pdfUrl = fileService.getPath(documentId);
            if (mindmapDto == null) {
                sseService.notifyKeywords(documentId, pdfUrl);
                sseService.notifySummary(documentId, pdfUrl);
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
        } else {
            String url = fileService.getPath(documentId);
            if (mindmapDto == null) {
                sseService.notifySTT(documentId, url, userId);
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>(mindmapDto, HttpStatus.OK);
    }


    @PutMapping("/documents/{documentID}/mindmap/keyword")
    @Operation(summary = "키워드 업데이트", description = "키워드 추가 및 삭제")
    public ResponseEntity<?> updateKeywords(@PathVariable(value = "documentID") long documentId, @RequestHeader("Authorization") String accessToken, @RequestBody KeywordsDto keywordsDto) {
        Keyword keywords = keywordService.updateKeywords(keywordsDto, documentId);
        sseService.notify(documentId, keywords.getKeywords());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/documents/{documentID}/mindmap/keyword/{keywordLabel}")
    @Operation(summary = "키워드 질문", description = "키워드 AI에 질문")
    public ResponseEntity<?> getAnswer(@PathVariable(value = "documentID") long documentId, @PathVariable String keywordLabel, @RequestHeader("Authorization") String accessToken) throws JsonProcessingException {
        //ResponseEntity<AnswerDto> responseEntity = requestService.requestAnswer(documentId, keywordLabel);
        sseService.notifyAnswer(documentId, keywordLabel);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "documents/{documentID}/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable(value = "documentID") long documentId) {
        return sseService.subscribe(documentId);
    }

    @PostMapping("/send-data/{documentID}")
    public void sendData(@PathVariable(value = "documentID") long documentId, @RequestBody KeywordsBodyDto keywordsBodyDto) {
        sseService.notify(documentId, keywordsBodyDto.getKeywords());
    }

    @PostMapping("/abc/{documentID}")
    public ResponseEntity<?> test(@PathVariable(value = "documentID") long documentId) throws UnsupportedAudioFileException, IOException {
        System.out.println(documentId);
        if (whiteboardService.isPdfType(documentId)) {
//            String path = fileService.getPdfUrl(documentId);
//            System.out.println(path);
            //ResponseEntity<String> body = requestService.requestPdfMindmap(path, documentId, keywords);
            //return new ResponseEntity<>(body, HttpStatus.OK);
        } else if (whiteboardService.isAudioType(documentId)) {
            String text = lectureNoteService.getText(documentId);
            System.out.println(text);
            //ResponseEntity<String> body = requestService.requestSTTMindmap(text, documentId, keywords);
            //return new ResponseEntity<>(body, HttpStatus.OK);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
