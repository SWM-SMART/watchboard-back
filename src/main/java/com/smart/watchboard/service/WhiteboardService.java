package com.smart.watchboard.service;

import com.smart.watchboard.domain.*;
import com.smart.watchboard.dto.*;
import com.smart.watchboard.repository.DocumentRepository;
import com.smart.watchboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhiteboardService {
    private final JwtService jwtService;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public List<DocumentDto> findDocumentsByUserId(String accessToken) {
        String extractedAccessToken = jwtService.extractAccessToken(accessToken);
        Optional<Long> userId = jwtService.extractUserId(extractedAccessToken);

        List<Document> documents = documentRepository.findDocumentsByUserId(userId.get());
        List<DocumentDto> documentDtos = new ArrayList<>();
        for (Document document : documents) {
            documentDtos.add(new DocumentDto(document.getDocumentId(), document.getDocumentName(), document.getCreatedAt(), document.getModifiedAt()));
        }

        return documentDtos;
    }

    public DocumentCreatedResponseDto createDocument(RequestCreatedDocumentDto requestCreatedDocumentDto, String accessToken) {
        String extractedAccessToken = jwtService.extractAccessToken(accessToken);
        Optional<Long> userId = jwtService.extractUserId(extractedAccessToken);
        User user = userRepository.getById(userId);

        Document document = new Document(requestCreatedDocumentDto.getDocumentName(), Instant.now().toEpochMilli(), Instant.now().toEpochMilli(), false, "none", user);
        documentRepository.save(document);
//        createUserDocument(user, document);

//        Map<String, WhiteboardData> documentData = new HashMap<>();
//        Whiteboard whiteboard = setWhiteboard(document, documentData);
//        whiteboardRepository.save(whiteboard);
        DocumentCreatedResponseDto responseDto = new DocumentCreatedResponseDto(document.getDocumentId(), document.getDocumentName(), document.getCreatedAt(), document.getModifiedAt());

        return responseDto;
    }

//    public void createUserDocument(User user, Document document) {
//        UserDocument userDocument = new UserDocument(user, document);
//        userDocumentRepository.save(userDocument);
//    }

    public void deleteDocument(long documentId, String accessToken) {
        String extractedAccessToken = jwtService.extractAccessToken(accessToken);
        jwtService.extractUserId(extractedAccessToken);
        Optional<Document> document = documentRepository.findById(documentId);
        if (document.isPresent()) {
            Document updatedDocument = document.get();
            updatedDocument.setDeleted(true);
            documentRepository.save(updatedDocument);
        }

    }

//    public void createWhiteboardData(Map<String, WhiteboardData> documentData, long documentId, String accessToken) {
//        String extractedAccessToken = jwtService.extractAccessToken(accessToken);
//        Optional<Long> userId = jwtService.extractUserId(extractedAccessToken);
//        User user = userRepository.getById(userId);
//        Document document = documentRepository.findByDocumentId(documentId);
//
//        if (!userDocumentRepository.existsByUserAndDocument(user, document)) {
//            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
//        }
//
//        Optional<Whiteboard> exsistingWhiteboardData = whiteboardRepository.findByDocumentId(document.getDocumentId());
//        exsistingWhiteboardData.ifPresentOrElse(data -> {
//            data.setDocumentData(documentData);
//            whiteboardRepository.save(data);
//            }, () -> {
//            Whiteboard whiteboard = setWhiteboard(document, documentData);
//            whiteboardRepository.save(whiteboard);
//        });
//    }

//    public Whiteboard setWhiteboard(Document document, Map<String, WhiteboardData> documentData) {
//        Whiteboard whiteboard = Whiteboard.builder()
//                .documentId(document.getDocumentId())
//                .documentName(document.getDocumentName())
//                .createdAt(document.getCreatedAt().toEpochMilli())
//                .modifiedAt(document.getModifiedAt().toEpochMilli())
//                .documentData(documentData)
//                .build();
//
//        return whiteboard;
//    }

//    public Map<String, DocumentObjectDto> setDocumentDataMap(Optional<Whiteboard> whiteboard) {
//        Map<String, DocumentObjectDto> documentDataMap = new HashMap<>();
//
//        for (Map.Entry<String, WhiteboardData> entry : whiteboard.get().getDocumentData().entrySet()) {
//            String key = entry.getKey();
//            WhiteboardData value = entry.getValue();
//
//            DocumentObjectDto documentObjectDto = DocumentObjectDtoFactory.createDtoFromWhiteboardData(value);
//            if (documentObjectDto != null) {
//                documentDataMap.put(key, documentObjectDto);
//            }
//        }
//
//        return documentDataMap;
//    }

    public DocumentResponseDto findDocument(long documentId, String accessToken) {
        String extractedAccessToken = jwtService.extractAccessToken(accessToken);
        Optional<Long> userId = jwtService.extractUserId(extractedAccessToken);
        //Optional<Whiteboard> whiteboard = whiteboardRepository.findByDocumentId(documentId);
        User user = userRepository.getById(userId);
        //Document document = documentRepository.findByDocumentId(whiteboard.get().getDocumentId());
        Document document = documentRepository.findByDocumentId(documentId);
//        if (!userDocumentRepository.existsByUserAndDocument(user, document)) {
//            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
//        }

        //String dataType = mindmapService.getDataType(documentId);

        DocumentResponseDto documentResponseDto = new DocumentResponseDto();
        documentResponseDto.setDocumentId(documentId);
//        documentResponseDto.setDocumentName(whiteboard.get().getDocumentName());
//        documentResponseDto.setCreatedAt(whiteboard.get().getCreatedAt());
//        documentResponseDto.setModifiedAt(whiteboard.get().getModifiedAt());
//        documentResponseDto.setDataType(dataType);

        documentResponseDto.setDocumentName(document.getDocumentName());
        documentResponseDto.setCreatedAt(document.getCreatedAt());
        documentResponseDto.setModifiedAt(document.getModifiedAt());
        documentResponseDto.setDataType(document.getDataType());

//        Map<String, DocumentObjectDto> documentDataMap = setDocumentDataMap(whiteboard);
//        documentResponseDto.setDocumentData(documentDataMap);

        return documentResponseDto;
    }

    public Document findDoc(long documentId) {
        Document document = documentRepository.findByDocumentId(documentId);
        return document;
    }

//    private DocumentCreatedResponseDto convertToDocumentCreatedResponseDto(Document document) {
//        DocumentCreatedResponseDto dto = new DocumentCreatedResponseDto(document.getDocumentId(), document.getDocumentName(), document.getCreatedAt().toEpochMilli(), document.getModifiedAt().toEpochMilli());
//        return dto;
//    }
}
