package com.smart.watchboard.service;

import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.User;
import com.smart.watchboard.domain.UserDocument;
import com.smart.watchboard.dto.DocumentCreatedResponseDto;
import com.smart.watchboard.dto.DocumentDto;
import com.smart.watchboard.dto.RequestCreatedDocumentDto;
import com.smart.watchboard.repository.DocumentRepository;
import com.smart.watchboard.repository.UserDocumentRepository;
import com.smart.watchboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhiteboardService {
    private final JwtService jwtService;
    private final DocumentRepository documentRepository;
    private final UserDocumentRepository userDocumentRepository;
    private final UserRepository userRepository;

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

    private DocumentCreatedResponseDto convertToDocumentCreatedResponseDto(Document document) {
        DocumentCreatedResponseDto dto = new DocumentCreatedResponseDto(document.getDocumentId(), document.getDocumentName(), document.getCreatedAt().toEpochMilli(), document.getModifiedAt().toEpochMilli());
        return dto;
    }
}
