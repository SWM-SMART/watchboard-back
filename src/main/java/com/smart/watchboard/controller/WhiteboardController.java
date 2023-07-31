package com.smart.watchboard.controller;

import com.smart.watchboard.domain.Document;
import com.smart.watchboard.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/documents")
@Tag(name = "화이트보드 문서 API", description = "화이트보드 관련 API(mock)")
public class WhiteboardController {

    @GetMapping()
    @Operation(summary = "화이트보드 목록 조회", description = "사용자가 속해 있는 모든 화이트보드 목록을 조회한다.")
    public ResponseEntity<?> getAllDocuments(@RequestHeader("accessToken") String accessToken) {
        List<Document> documents = new ArrayList<>();

        documents.add(new Document(1L, "문서1", 1690469974169L, 1690470003987L));
        documents.add(new Document(2L, "문서2", 1690470060729L, 1690470071589L));
        documents.add(new Document(3L, "문서3", 1690470080683L, 1690470091336L));
        documents.add(new Document(4L, "문서4", 1690470100340L, 1690470108837L));
        documents.add(new Document(5L, "문서5", 1690470121154L, 1690470129636L));

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/{documentID}")
    @Operation(summary = "화이트보드 데이터 조회", description = "특정 화이트보드의 데이터를 조회한다.")
    public ResponseEntity<?> getDocument(@RequestParam("documentID") long documentId, @RequestParam("accessToken") String accessToken) {
        DocumentResponseDto response = new DocumentResponseDto();
        response.setDocument_id(documentId);
        response.setDocument_name("document1");
        response.setCreated_at(1689742186901L);
        response.setModified_at(1689828586901L);

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
    public ResponseEntity<?> createDocument(@RequestBody RequestCreatedDocumentDto requestCreatedDocumentDto) {
        String documentName = requestCreatedDocumentDto.getDocumentName();
        DocumentCreatedResponseDto documentCreatedResponseDto = new DocumentCreatedResponseDto(2133L, documentName, 1690528202374L, 1690528216601L);

        return new ResponseEntity<>(documentCreatedResponseDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{documentID}")
    @Operation(summary = "화이트보드 삭제", description = "특정 화이트보드를 삭제한다.")
    public ResponseEntity<?> deleteDocument(@RequestParam("documentID") long documentId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
