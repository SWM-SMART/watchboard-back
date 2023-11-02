package com.smart.watchboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.Mindmap;
import com.smart.watchboard.dto.MindmapDto;
import com.smart.watchboard.repository.MindmapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MindmapService {

    private final MindmapRepository mindmapRepository;
    private final WhiteboardService whiteboardService;

    public void createMindmap(ResponseEntity<String> responseEntity, Long documentId, String dataType) throws JsonProcessingException {
        Document document = whiteboardService.findDoc(documentId);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        Integer root = jsonNode.get("root").asInt();
        JsonNode keywordsNode = jsonNode.get("keywords");
        List<String> keywords = objectMapper.convertValue(keywordsNode, new TypeReference<List<String>>() {});
        JsonNode graphNode = jsonNode.get("graph");
        Map<String, List<Integer>> graphMap = objectMapper.convertValue(graphNode, new TypeReference<Map<String, List<Integer>>>() {});

        Mindmap mindmap = Mindmap.builder()
                .documentId(documentId)
                .documentName(document.getDocumentName())
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .dataType(dataType)
                .root(root)
                .keywords(keywords)
                .graph(graphMap)
                .build();

        mindmapRepository.save(mindmap);
    }

    public MindmapDto getMindmap(Long documentId) {
        Optional<Mindmap> mindmap = mindmapRepository.findByDocumentId(documentId);
        MindmapDto mindmapDto = new MindmapDto(mindmap.get().getRoot(), mindmap.get().getKeywords(), mindmap.get().getGraph());

        return mindmapDto;
    }

    public void deleteMindmap(Long documentId) {
        mindmapRepository.deleteByDocumentId(documentId);
    }
}