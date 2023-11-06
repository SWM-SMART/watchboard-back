package com.smart.watchboard.controller;

import com.smart.watchboard.domain.WhiteboardData;
import com.smart.watchboard.dto.*;
import com.smart.watchboard.service.WhiteboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/documents")
@Tag(name = "문서 API", description = "문서 관련 API(mock)")
@RequiredArgsConstructor
public class WhiteboardController {

    private final WhiteboardService whiteboardService;

    @GetMapping()
    @Operation(summary = "문서 목록 조회", description = "사용자가 속해 있는 모든 문서 목록을 조회한다.")
    public ResponseEntity<?> getAllDocuments(@RequestHeader("Authorization") String accessToken) {
        List<DocumentDto> documents = whiteboardService.findDocumentsByUserId(accessToken);

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/{documentID}")
    @Operation(summary = "문서 데이터 조회", description = "특정 문서의 데이터를 조회한다.")
    public ResponseEntity<DocumentResponseDto> getDocument(@PathVariable(value = "documentID") long documentId, @RequestHeader("Authorization") String accessToken) {
        DocumentResponseDto response = whiteboardService.findDocument(documentId, accessToken);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping()
    @Operation(summary = "문서 생성", description = "화이트보드를 생성한다.")
    public ResponseEntity<?> createDocument(@RequestBody RequestCreatedDocumentDto requestCreatedDocumentDto, @RequestHeader("Authorization") String accessToken) {
        DocumentCreatedResponseDto documentCreatedResponseDto = whiteboardService.createDocument(requestCreatedDocumentDto, accessToken);

        return new ResponseEntity<>(documentCreatedResponseDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{documentID}")
    @Operation(summary = "문서 삭제", description = "특정 화이트보드를 삭제한다.")
    public ResponseEntity<?> deleteDocument(@PathVariable(value = "documentID") long documentId, @RequestHeader("Authorization") String accessToken) {
        whiteboardService.deleteDocument(documentId, accessToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PostMapping("/{documentID}/data")
//    @Operation(summary = "화이트보드 문서 데이터 생성 및 수정", description = "화이트보드 문서 내의 데이터 생성 및 수정한다.")
//    public ResponseEntity<?> createDocumentData(@PathVariable(value = "documentID") long documentId, @RequestHeader("Authorization") String accessToken, @RequestBody Map<String, WhiteboardData> documentData) {
//        whiteboardService.createWhiteboardData(documentData, documentId, accessToken);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
