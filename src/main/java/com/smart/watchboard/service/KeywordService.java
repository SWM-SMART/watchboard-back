package com.smart.watchboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.Keyword;
import com.smart.watchboard.domain.Mindmap;
import com.smart.watchboard.dto.KeywordsBodyDto;
import com.smart.watchboard.dto.KeywordsDto;
import com.smart.watchboard.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeywordService {
    private final WhiteboardService whiteboardService;
    private final KeywordRepository keywordRepository;

    public List<String> createKeywords(ResponseEntity<KeywordsBodyDto> responseEntity, Long documentId) throws JsonProcessingException {
        Document document = whiteboardService.findDoc(documentId);
        // 마인드맵 생성

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody().getKeywords().toString());
        JsonNode keywordsNode = jsonNode.get("keywords");
        List<String> keywords = objectMapper.convertValue(keywordsNode, new TypeReference<List<String>>() {});

        Keyword keyword = Keyword.builder()
                .documentId(documentId)
                .keywords(keywords)
                .build();

        keywordRepository.save(keyword);

        return keywords;
    }

    public List<String> renewKeywords(ResponseEntity<KeywordsBodyDto> responseEntity, Long documentId) throws JsonProcessingException {
        Document document = whiteboardService.findDoc(documentId);
        // 마인드맵 생성
        Optional<Keyword> keyword = keywordRepository.findByDocumentId(documentId);
        Keyword formerKeyword = Keyword.builder()
                .objectId(keyword.get().getObjectId())
                .documentId(keyword.get().getDocumentId())
                .keywords(keyword.get().getKeywords())
                .build();
        deleteAllKeywords(formerKeyword);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody().getKeywords().toString());
        JsonNode keywordsNode = jsonNode.get("keywords");
        List<String> keywords = objectMapper.convertValue(keywordsNode, new TypeReference<List<String>>() {});

        Keyword newKeyword = Keyword.builder()
                .documentId(documentId)
                .keywords(keywords)
                .build();

        keywordRepository.save(newKeyword);

        return keywords;
    }

    public void updateKeywords(KeywordsDto keywordsDto, Long documentId) {
        Optional<Keyword> keyword = keywordRepository.findByDocumentId(documentId);
        List<String> keywords = keyword.get().getKeywords();
        List<String> addKeywords = keywordsDto.getAdd();
        List<String> deleteKeywords = keywordsDto.getDelete();

        List<String> newKeywords = new ArrayList<>(keywords);
        newKeywords.removeAll(deleteKeywords);
        newKeywords.addAll(addKeywords);

        Keyword updatedKeyword = keyword.orElse(null);
        updatedKeyword.setKeywords(newKeywords);

        keywordRepository.save(updatedKeyword);
    }

    public void deleteAllKeywords(Keyword keyword) {
        keywordRepository.delete(keyword);
    }
}
