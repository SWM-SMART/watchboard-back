package com.smart.watchboard.service;

import com.smart.watchboard.domain.Note;
import com.smart.watchboard.dto.MindmapResponseDto;
import com.smart.watchboard.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SseService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final FileService fileService;
    private final WhiteboardService whiteboardService;
    private final RequestService requestService;
    private final LectureNoteService lectureNoteService;
    private final NoteService noteService;

    public SseEmitter subscribe(Long documentId) {
        SseEmitter emitter = createEmitter(documentId);

        sendToClientFirst(documentId, "EventStream Created. [documentId=" + documentId + "]");
        return emitter;
    }

    public void notify(Long documentId, List<String> keywords) {
        sendToClient(documentId, keywords);
    }

    private void sendToClientFirst(Long documentId, Object data) {
        SseEmitter emitter = emitterRepository.get(documentId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().id(String.valueOf(documentId)).name("sse").data(data));
            } catch (IOException exception) {
                emitterRepository.deleteById(documentId);
                emitter.completeWithError(exception);
            }
        }
    }

    private void sendToClient(Long documentId, List<String> keywords) {
        SseEmitter emitter = emitterRepository.get(documentId);
        if (emitter != null) {
            try {
                if (whiteboardService.isPdfType(documentId)) {
                    String path = fileService.getPdfUrl(documentId);
                    ResponseEntity<MindmapResponseDto> body = requestService.requestPdfMindmap(path, documentId, keywords);
                    emitter.send(SseEmitter.event().id(String.valueOf(documentId)).name("sse").data(body.getBody()));
                } else if (whiteboardService.isAudioType(documentId)) {
                    Note note = noteService.findByDocument(documentId);
                    ResponseEntity<MindmapResponseDto> body = requestService.requestSTTMindmap(note.getPath(), documentId, keywords);
                    emitter.send(SseEmitter.event().id(String.valueOf(documentId)).name("sse").data(body.getBody()));
                }
            } catch (IOException exception) {
                emitterRepository.deleteById(documentId);
                emitter.completeWithError(exception);
            }
        }
    }

    private SseEmitter createEmitter(Long documentId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(documentId, emitter);

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() -> emitterRepository.deleteById(documentId));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> emitterRepository.deleteById(documentId));

        return emitter;
    }
}
