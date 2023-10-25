package com.smart.watchboard.service;

import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.Note;
import com.smart.watchboard.domain.Summary;
import com.smart.watchboard.repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {

    private final SummaryRepository summaryRepository;
    private final WhiteboardService whiteboardService;

    public void createSummary(Long documentId, String summaryContent) {
        Document document = whiteboardService.findDoc(documentId);
        Summary summary = Summary.builder()
                .content(summaryContent)
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .isDelete(false)
                .document(document)
                .build();

        summaryRepository.save(summary);
    }

    public void updateSummary(Long documentId, String summaryContent) {
        Document document = whiteboardService.findDoc(documentId);
        Summary summary = summaryRepository.findByDocument(document);
        summary.setContent(summaryContent);
        summary.setModifiedAt(Instant.now());

        summaryRepository.save(summary);
    }

    public void deleteSummary(Long documentId) {
        Document document = whiteboardService.findDoc(documentId);
        Summary summary = summaryRepository.findByDocument(document);
        summaryRepository.delete(summary);
    }

}
