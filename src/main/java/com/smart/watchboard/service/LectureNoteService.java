package com.smart.watchboard.service;

import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.LectureNote;
import com.smart.watchboard.domain.Note;
import com.smart.watchboard.domain.SttData;
import com.smart.watchboard.repository.LectureNoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureNoteService {
    private final LectureNoteRepository lectureNoteRepository;
    private WhiteboardService whiteboardService;

    public void createLectureNote(Long documentId, List<SttData> data) {
        //Document document = whiteboardService.findDoc(documentId);
        LectureNote lectureNote = LectureNote.builder()
                .documentId(documentId)
                .data(data)
                .build();

        lectureNoteRepository.save(lectureNote);
    }

    public void updateLectureNote(Long documentId, List<SttData> data) {
        //Document document = whiteboardService.findDoc(documentId);
        LectureNote lectureNote = LectureNote.builder()
                .documentId(documentId)
                .data(data)
                .build();

        lectureNoteRepository.save(lectureNote);
    }

    public List<SttData> getData(Long documentId) {
        LectureNote lectureNote = lectureNoteRepository.findByDocumentId(documentId);
        return lectureNote.getData();
    }
}
