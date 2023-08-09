package com.smart.watchboard.controller;

import com.smart.watchboard.dto.*;
import com.smart.watchboard.service.WhiteboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/documents")
@Tag(name = "화이트보드 문서 API", description = "화이트보드 관련 API(mock)")
@RequiredArgsConstructor
public class WhiteboardController {

    private final WhiteboardService whiteboardService;

    @GetMapping()
    @Operation(summary = "화이트보드 목록 조회", description = "사용자가 속해 있는 모든 화이트보드 목록을 조회한다.")
    public ResponseEntity<?> getAllDocuments(@RequestHeader("Authorization") String accessToken) {
        List<DocumentDto> documents = whiteboardService.findDocumentsByUserId(accessToken);

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/{documentID}")
    @Operation(summary = "화이트보드 데이터 조회", description = "특정 화이트보드의 데이터를 조회한다.")
    public ResponseEntity<?> getDocument(@PathVariable(value = "documentID") long documentId) {
        DocumentResponseDto response = new DocumentResponseDto();
        response.setDocumentId(documentId);
        response.setDocumentName("document1");
        response.setCreatedAt(1689742186901L);
        response.setModifiedAt(1689828586901L);

        Map<String, DocumentObjectDto> documentDataMap = new HashMap<>();

        documentDataMap.put("0", new DocumentRectDto("0", "RECT", 10, 10, 0.0001, "ROOT", 100, 100, "rgb(121, 75, 150)"));
        documentDataMap.put("1", new DocumentRectDto("1", "RECT", -10, -10, 0.0002, "ROOT", 100, 100, "rgb(20, 200, 100)"));
        documentDataMap.put("2", new DocumentTextDto("2", "TEXT", 0, 0, 0.0003, "ROOT", 100, 5, "normal", "lorem ipsum", "rgb(121, 75, 150)"));
        documentDataMap.put("3", new DocumentTextDto("3", "TEXT", 0, 0, 0.0003, "1", 100, 5, "normal", "lorem ipsum", "rgb(121, 75, 150)"));
        documentDataMap.put("4", new DocumentTextDto("4", "TEXT", 0, 0, 0.0003, "2", 100, 5, "normal", "lorem ipsum", "rgb(121, 75, 150)"));
        documentDataMap.put("5", new DocumentTextDto("5", "TEXT", 0, 0, 0.0003, "6", 100, 5, "normal", "lorem ipsum", "rgb(121, 75, 150)"));
        documentDataMap.put("6", new DocumentTextDto("6", "TEXT", 0, 0, 0.0003, "2", 100, 5, "normal", "lorem ipsum", "rgb(121, 75, 150)"));
        documentDataMap.put("7", new DocumentTextDto("7", "TEXT", 0, 0, 0.0003, "1", 100, 5, "normal", "lorem ipsum", "rgb(121, 75, 150)"));
        documentDataMap.put("8", new DocumentTextDto("8", "TEXT", 0, 0, 0.0003, "3", 100, 5, "normal", "lorem ipsum", "rgb(121, 75, 150)"));

        response.setDocumentData(documentDataMap);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping()
    @Operation(summary = "화이트보드 생성", description = "화이트보드를 생성한다.")
    public ResponseEntity<?> createDocument(@RequestBody RequestCreatedDocumentDto requestCreatedDocumentDto, @RequestHeader("Authorization") String accessToken) {
        DocumentCreatedResponseDto documentCreatedResponseDto = whiteboardService.createDocument(requestCreatedDocumentDto, accessToken);

        return new ResponseEntity<>(documentCreatedResponseDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{documentID}")
    @Operation(summary = "화이트보드 삭제", description = "특정 화이트보드를 삭제한다.")
    public ResponseEntity<?> deleteDocument(@PathVariable(value = "documentID") long documentId, @RequestHeader("Authorization") String accessToken) {
        whiteboardService.deleteDocument(documentId, accessToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
