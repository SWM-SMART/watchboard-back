package com.smart.watchboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.Keyword;
import com.smart.watchboard.domain.Mindmap;
import com.smart.watchboard.dto.MindmapDto;
import com.smart.watchboard.dto.MindmapResponseDto;
import com.smart.watchboard.repository.MindmapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MindmapService {

    private final MindmapRepository mindmapRepository;
    private final WhiteboardService whiteboardService;
    private final KeywordService keywordService;

    public void createMindmap(ResponseEntity<MindmapResponseDto> responseEntity, Long documentId) throws JsonProcessingException {
        Document document = whiteboardService.findDoc(documentId);

//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
//        Integer root = jsonNode.get("root").asInt();
//        JsonNode keywordsNode = jsonNode.get("keywords");
//        List<String> keywords = objectMapper.convertValue(keywordsNode, new TypeReference<List<String>>() {});
//        JsonNode graphNode = jsonNode.get("graph");
//        Map<String, List<Integer>> graphMap = objectMapper.convertValue(graphNode, new TypeReference<Map<String, List<Integer>>>() {});

        Optional<Mindmap> formerMindmap = mindmapRepository.findByDocumentId(documentId);
        if (formerMindmap.isPresent()) {
            Mindmap mindmap = Mindmap.builder()
                    .objectId(formerMindmap.get().getObjectId())
                    .documentId(documentId)
                    .documentName(document.getDocumentName())
                    .createdAt(formerMindmap.get().getCreatedAt())
                    .modifiedAt(Instant.now())
                    .root(responseEntity.getBody().getRoot())
                    .graph(responseEntity.getBody().getGraph())
                    .build();
            mindmapRepository.save(mindmap);
        } else if (!formerMindmap.isPresent()) {
            Mindmap mindmap = Mindmap.builder()
                    .documentId(documentId)
                    .documentName(document.getDocumentName())
                    .createdAt(Instant.now())
                    .modifiedAt(Instant.now())
                    .root(responseEntity.getBody().getRoot())
                    .graph(responseEntity.getBody().getGraph())
                    .build();
            mindmapRepository.save(mindmap);
        }
    }

    public MindmapDto getMindmap(Long documentId) {
        Optional<Mindmap> mindmap = mindmapRepository.findByDocumentId(documentId);
        Keyword keyword = keywordService.findKeywords(documentId);

        if (mindmap.isEmpty()) {
            return null;
        }
        MindmapDto mindmapDto = new MindmapDto(mindmap.get().getRoot(), keyword.getKeywords(), mindmap.get().getGraph());

        return mindmapDto;
    }

    public void deleteMindmap(Long documentId) {
        mindmapRepository.deleteByDocumentId(documentId);
    }
}
