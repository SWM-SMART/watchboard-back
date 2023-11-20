package com.smart.watchboard.service;

import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.File;
import com.smart.watchboard.domain.Note;
import com.smart.watchboard.dto.FileDto;
import com.smart.watchboard.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService {
    private final NoteRepository noteRepository;
    private final WhiteboardService whiteboardService;

    public void createNote(FileDto fileDto) {
        Document document = whiteboardService.findDoc(fileDto.getDocumentId());
        String key = fileDto.getFileType() + "/" + fileDto.getDocumentId() + "." + fileDto.getFile().getOriginalFilename();
        Note note = Note.builder()
                .fileName(fileDto.getFile().getOriginalFilename())
                .objectKey(key)
                .path(fileDto.getPath())
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .isDelete(false)
                .document(document)
                .build();

        noteRepository.save(note);
    }

    public void updateNote(FileDto fileDto) {
        Document document = whiteboardService.findDoc(fileDto.getDocumentId());
        Note note = findNote(document);
        note.setFileName(fileDto.getFile().getOriginalFilename());
        note.setPath(fileDto.getPath());
        note.setModifiedAt(Instant.now());

        noteRepository.save(note);
    }

    public void deleteNote(Long documentId) {
        Document document = whiteboardService.findDoc(documentId);
        Note note = findNote(document);
        noteRepository.delete(note);
    }

    public Note findNote(Document document) {
        return noteRepository.findByDocument(document);
    }

    public Note findByDocument(Long documentId) {
        Document document = whiteboardService.findDoc(documentId);
        return noteRepository.findByDocument(document);
    }
}
