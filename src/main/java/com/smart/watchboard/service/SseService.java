package com.smart.watchboard.service;

import com.itextpdf.text.DocumentException;
import com.smart.watchboard.common.support.AwsS3Uploader;
import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.Note;
import com.smart.watchboard.domain.SttData;
import com.smart.watchboard.dto.*;
import com.smart.watchboard.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static com.smart.watchboard.common.support.PdfConverter.convertStringToPdf;

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
    private final KeywordService keywordService;
    private final SummaryService summaryService;
    private final STTService sttService;
    private final AwsS3Uploader awsS3Uploader;
    private final QuestionService questionService;

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
                    String path = fileService.getPath(documentId);
                    ResponseEntity<MindmapResponseDto> body = requestService.requestPdfMindmap(path, documentId, keywords);
                    emitter.send(SseEmitter.event().id(String.valueOf(documentId)).name("mindmap").data("mindmap"));
                } else if (whiteboardService.isAudioType(documentId)) {
                    Note note = noteService.findByDocument(documentId);
                    ResponseEntity<MindmapResponseDto> body = requestService.requestSTTMindmap(note.getPath(), documentId, keywords);
                    emitter.send(SseEmitter.event().id(String.valueOf(documentId)).name("mindmap").data("mindmap"));
                }
            } catch (IOException exception) {
                emitterRepository.deleteById(documentId);
                emitter.completeWithError(exception);
            }
        }
    }

    public void notifyKeywords(Long documentId, String path) {
        sendKeywords(documentId, path);
    }

    public void notifySummary(Long documentId, String path) {
        sendSummary(documentId, path);
    }

    public void notifyAnswer(Long documentId, String keywordLabel) {
        sendAnswer(documentId, keywordLabel);
    }

    public void notifySTT(Long documentId, String path, Long userId) {
        Note note = noteService.findByDocument(documentId);
        if (note == null) {
            sendSTT(documentId, path, userId);
        } else {
            sendSTTUpdate(documentId, path, userId);
        }
    }

    private void sendKeywords(Long documentId, String path) {
        SseEmitter emitter = emitterRepository.get(documentId);
        if (emitter != null) {
            try {
                Document document = whiteboardService.findDoc(documentId);
                ResponseEntity<KeywordsBodyDto> responseEntity;
                if (document.getDataType().equals("pdf")) {
                    responseEntity = requestService.requestPdfKeywords(path);
                    if (keywordService.findKeywords(documentId) == null) {
                        keywordService.createKeywords(responseEntity, documentId);
                    } else {
                        keywordService.renewKeywords(responseEntity, documentId);
                    }
                    emitter.send(SseEmitter.event().id(String.valueOf(documentId)).name("keywords").data("keywords"));
                    sendToClient(documentId, responseEntity.getBody().getKeywords());
                } else {
                    responseEntity = requestService.requestSTTKeywords(path);
                    if (keywordService.findKeywords(documentId) == null) {
                        keywordService.createKeywords(responseEntity, documentId);
                    } else {
                        keywordService.renewKeywords(responseEntity, documentId);
                    }
                    emitter.send(SseEmitter.event().id(String.valueOf(documentId)).name("keywords").data("keywords"));
                    sendToClient(documentId, responseEntity.getBody().getKeywords());
                }
            } catch (IOException exception) {
                emitterRepository.deleteById(documentId);
                emitter.completeWithError(exception);
            }

        }
    }

    private void sendSummary(Long documentId, String path) {
        SseEmitter emitter = emitterRepository.get(documentId);
        if (emitter != null) {
            try {
                ResponseEntity<SummaryDto> summary = requestService.requestSTTSummary(path);
                if (summaryService.findSummary(documentId) == null) {
                    summaryService.createSummary(documentId, summary.getBody().getSummary());
                } else {
                    summaryService.updateSummary(documentId, summary.getBody().getSummary());
                }
            } catch (IOException exception) {
                emitterRepository.deleteById(documentId);
                emitter.completeWithError(exception);
            }
        }
    }

    private void sendAnswer(Long documentId, String keyword) {
        AnswerDto answerDto = questionService.getAnswer(documentId, keyword);
        if (answerDto == null) {
            answerDto.setText("processing");
            ResponseEntity<AnswerDto> temp = new ResponseEntity<>(answerDto, HttpStatus.OK);
            questionService.createAnswer(documentId, keyword, temp);
        }

        SseEmitter emitter = emitterRepository.get(documentId);
        if (emitter != null) {
            try {
                ResponseEntity<AnswerDto> responseEntity = requestService.requestAnswer(documentId, keyword);
                questionService.createAnswer(documentId, keyword, responseEntity);
                emitter.send(SseEmitter.event().id(String.valueOf(documentId)).name("answer").data(keyword));
            } catch (IOException exception) {
                emitterRepository.deleteById(documentId);
                emitter.completeWithError(exception);
            }
        }
    }

    private void sendSTT(Long documentId, String path, Long userId) {
        SseEmitter emitter = emitterRepository.get(documentId);
        if (emitter != null) {
            try {
                ResponseEntity<String> sttResponseEntity = sttService.getSTT(path);
                String sttResult = sttService.getText(sttResponseEntity);

                List<SttData> data = sttService.getSTTData(sttResponseEntity);
                lectureNoteService.createLectureNote(documentId, data, sttResult);
                SttDataDto sttDataDto = new SttDataDto(data);

                String sttFileName = String.valueOf(userId) + "_" + String.valueOf(documentId) + ".txt";
                File textFile = convertStringToPdf(sttResult, sttFileName);

                String contentType = "application/pdf";
                String originalFilename = textFile.getName();
                String name = textFile.getName();

                FileInputStream fileInputStream = new FileInputStream(textFile);
                MultipartFile multipartFile = new MockMultipartFile(name, originalFilename, contentType, fileInputStream);
                S3Dto s3DtoForSTT = new S3Dto(multipartFile, documentId, userId, "pdf");
                String textPdfPath = awsS3Uploader.uploadTextPdfFile(s3DtoForSTT);

                notifyKeywords(documentId, textPdfPath);
                notifySummary(documentId, textPdfPath);

                emitter.send(SseEmitter.event().id(String.valueOf(documentId)).name("audio"));
            } catch (IOException | DocumentException exception) {
                emitterRepository.deleteById(documentId);
                emitter.completeWithError(exception);
            }
        }
    }

    private void sendSTTUpdate(Long documentId, String path, Long userId) {
        SseEmitter emitter = emitterRepository.get(documentId);
        if (emitter != null) {
            try {
                ResponseEntity<String> sttResponseEntity = sttService.getSTT(path);
                String sttResult = sttService.getText(sttResponseEntity);

                List<SttData> data = sttService.getSTTData(sttResponseEntity);
                lectureNoteService.updateLectureNote(documentId, data, sttResult);
                SttDataDto sttDataDto = new SttDataDto(data);

                String sttFileName = String.valueOf(userId) + "_" + String.valueOf(documentId) + ".txt";
                File textPdfFile = convertStringToPdf(sttResult, sttFileName);

                String contentType = "application/pdf";
                String originalFilename = textPdfFile.getName();
                String name = textPdfFile.getName();

                FileInputStream fileInputStream = new FileInputStream(textPdfFile);
                MultipartFile multipartFile = new MockMultipartFile(name, originalFilename, contentType, fileInputStream);
                S3Dto s3DtoForSTT = new S3Dto(multipartFile, documentId, userId, "pdf");
                String textPdfPath = awsS3Uploader.uploadTextPdfFile(s3DtoForSTT);

                notifyKeywords(documentId, textPdfPath);
                notifySummary(documentId, textPdfPath);

                emitter.send(SseEmitter.event().id(String.valueOf(documentId)).name("audio"));
            } catch (IOException | DocumentException exception) {
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
