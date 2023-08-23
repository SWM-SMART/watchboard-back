package com.smart.watchboard.service;

import com.smart.watchboard.domain.*;
import com.smart.watchboard.dto.*;
import com.smart.watchboard.repository.DocumentRepository;
import com.smart.watchboard.repository.UserDocumentRepository;
import com.smart.watchboard.repository.UserRepository;
import com.smart.watchboard.repository.WhiteboardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhiteboardService {
    private final JwtService jwtService;
    private final DocumentRepository documentRepository;
    private final UserDocumentRepository userDocumentRepository;
    private final UserRepository userRepository;
    private final WhiteboardRepository whiteboardRepository;

    public List<DocumentDto> findDocumentsByUserId(String accessToken) {
        String extractedAccessToken = jwtService.extractAccessToken(accessToken);
        Optional<Long> userId = jwtService.extractUserId(extractedAccessToken);

        List<Document> documents = documentRepository.findDocumentsByUserId(userId.get());
        List<DocumentDto> documentDtos = new ArrayList<>();
        for (Document document : documents) {
            documentDtos.add(new DocumentDto(document.getDocumentId(), document.getDocumentName(), document.getCreatedAt().toEpochMilli(), document.getModifiedAt().toEpochMilli()));
        }

        return documentDtos;
    }

    public DocumentCreatedResponseDto createDocument(RequestCreatedDocumentDto requestCreatedDocumentDto, String accessToken) {
        String extractedAccessToken = jwtService.extractAccessToken(accessToken);
        Optional<Long> userId = jwtService.extractUserId(extractedAccessToken);
        User user = userRepository.getById(userId);

        Document document = new Document(requestCreatedDocumentDto.getDocumentName(), Instant.now(), Instant.now(), false);
        Document savedDocument = documentRepository.save(document);
        createUserDocument(user, document);

        Map<String, WhiteboardData> documentData = new HashMap<>();

        Whiteboard whiteboard = Whiteboard.builder()
                .documentId(document.getDocumentId())
                .documentName(document.getDocumentName())
                .createdAt(document.getCreatedAt().toEpochMilli())
                .modifiedAt(document.getModifiedAt().toEpochMilli())
                .documentData(documentData)
                .build();

        whiteboardRepository.save(whiteboard);

        return convertToDocumentCreatedResponseDto(savedDocument);
    }

    public void createUserDocument(User user, Document document) {
        UserDocument userDocument = new UserDocument(user, document);
        userDocumentRepository.save(userDocument);
    }

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

    public void createWhiteboardData(Map<String, WhiteboardData> documentData, long documentId, String accessToken) {
        String extractedAccessToken = jwtService.extractAccessToken(accessToken);
        Optional<Long> userId = jwtService.extractUserId(extractedAccessToken);
        User user = userRepository.getById(userId);
        Document document = documentRepository.findByDocumentId(documentId);

        if (!userDocumentRepository.existsByUserAndDocument(user, document)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }

        Optional<Whiteboard> exsistingWhiteboardData = whiteboardRepository.findByDocumentId(document.getDocumentId());
        exsistingWhiteboardData.ifPresentOrElse(data -> {
            data.setDocumentData(documentData);
            whiteboardRepository.save(data);
            }, () -> {
            Whiteboard whiteboard = Whiteboard.builder()
                    .documentId(documentId)
                    .documentName(document.getDocumentName())
                    .createdAt(document.getCreatedAt().toEpochMilli())
                    .modifiedAt(document.getModifiedAt().toEpochMilli())
                    .documentData(documentData)
                    .build();
            whiteboardRepository.save(whiteboard);
        });
    }

    public DocumentResponseDto findDocument(long documentId, String accessToken) {
        String extractedAccessToken = jwtService.extractAccessToken(accessToken);
        Optional<Long> userId = jwtService.extractUserId(extractedAccessToken);
        Optional<Whiteboard> whiteboard = whiteboardRepository.findByDocumentId(documentId);
        User user = userRepository.getById(userId);
        Document document = documentRepository.findByDocumentId(whiteboard.get().getDocumentId());
        if (!userDocumentRepository.existsByUserAndDocument(user, document)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }

        DocumentResponseDto documentResponseDto = new DocumentResponseDto();
        documentResponseDto.setDocumentId(documentId);
        documentResponseDto.setDocumentName(whiteboard.get().getDocumentName());
        documentResponseDto.setCreatedAt(whiteboard.get().getCreatedAt());
        documentResponseDto.setModifiedAt(whiteboard.get().getModifiedAt());

        Map<String, DocumentObjectDto> documentDataMap = new HashMap<>();
        for (Map.Entry<String, WhiteboardData> entry : whiteboard.get().getDocumentData().entrySet()) {
            String key = entry.getKey();
            WhiteboardData value = entry.getValue();

            if (value.getType().equals("TEXT")) {
                DocumentTextDto documentTextDto = DocumentTextDto.builder()
                        .objId(value.getObjId())
                        .type(value.getType())
                        .x(value.getX())
                        .y(value.getY())
                        .depth(value.getDepth())
                        .parentId(value.getParentId())
                        .w(value.getW())
                        .fontSize(value.getFontSize())
                        .overflow(value.getOverflow())
                        .text(value.getText())
                        .color(value.getColor())
                        .build();
                documentDataMap.put(key, documentTextDto);
            } else if (value.getType().equals("RECT")) {
                DocumentRectDto documentRectDto = DocumentRectDto.builder()
                        .objId(value.getObjId())
                        .type(value.getType())
                        .x(value.getX())
                        .y(value.getY())
                        .depth(value.getDepth())
                        .parentId(value.getParentId())
                        .w(value.getW())
                        .h(value.getH())
                        .color(value.getColor())
                        .build();
                documentDataMap.put(key, documentRectDto);
            } else if (value.getType().equals("LINE")) {
                DocumentLineDto documentLineDto = DocumentLineDto.builder()
                        .objId(value.getObjId())
                        .type(value.getType())
                        .x(value.getX())
                        .y(value.getY())
                        .depth(value.getDepth())
                        .parentId(value.getParentId())
                        .w(value.getW())
                        .h(value.getH())
                        .fontSize(value.getFontSize())
                        .color(value.getColor())
                        .build();
                documentDataMap.put(key, documentLineDto);
            }
        }
        documentResponseDto.setDocumentData(documentDataMap);

        return documentResponseDto;
    }

    private DocumentCreatedResponseDto convertToDocumentCreatedResponseDto(Document document) {
        DocumentCreatedResponseDto dto = new DocumentCreatedResponseDto(document.getDocumentId(), document.getDocumentName(), document.getCreatedAt().toEpochMilli(), document.getModifiedAt().toEpochMilli());
        return dto;
    }
}
