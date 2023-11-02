package com.smart.watchboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.File;
import com.smart.watchboard.domain.SttData;
import com.smart.watchboard.dto.FileDto;
import com.smart.watchboard.dto.SttDto;
import com.smart.watchboard.repository.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRepository fileRepository;
    private final WhiteboardService whiteboardService;

    public void createFile(FileDto fileDto) {
        Document document = whiteboardService.findDoc(fileDto.getDocumentId());
        String key = fileDto.getFileType() + "/" + fileDto.getDocumentId() + "." + fileDto.getFile().getOriginalFilename();
        File file = File.builder()
                .fileName(fileDto.getFile().getOriginalFilename())
                .objectKey(key)
                .path(fileDto.getPath())
                .fileType(fileDto.getFileType())
                .size(fileDto.getFile().getSize())
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .isDelete(false)
                .document(document)
                .build();

        fileRepository.save(file);
    }

    public void updateFile(FileDto fileDto) {
        Optional<File> file = findFile(fileDto.getFileId());
        File updatedFile = file.get();
        updatedFile.setFileName(fileDto.getFile().getOriginalFilename());
        updatedFile.setPath(fileDto.getPath());
        updatedFile.setSize(fileDto.getFile().getSize());
        updatedFile.setCreatedAt(Instant.now());
        updatedFile.setModifiedAt(Instant.now());
        fileRepository.save(updatedFile);
    }

    public void deleteFile(long fileId) {
        Optional<File> file = findFile(fileId);
        File deletedFile = file.get();
        deletedFile.setDelete(true);
        fileRepository.save(deletedFile);
    }

    public Optional<File> findFile(long fileId) {
        Optional<File> file = fileRepository.findById(fileId);

        return file;
    }

    public String getPdfUrl(Long documentId) {
        Document document = whiteboardService.findDoc(documentId);
        File file = fileRepository.findByDocument(document);
        String body = """
                {
                    "url": %s
                }
                """.formatted(file.getPath());
        return body;
    }

//    public String createResponseBody2(ResponseEntity<String> keywordResponseEntity, String text) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(keywordResponseEntity.getBody());
//        ((ObjectNode) jsonNode).put("text", text);
//        String updatedJsonString = jsonNode.toString();
//
//        return updatedJsonString;
//
//    }

    public SttDto createResponseBody(String path, List<SttData> data) throws JsonProcessingException {
        SttDto sttDto = new SttDto(path, data);
        return sttDto;
    }

    public void deleteAudioFile(Long documentId) {
        Document document = whiteboardService.findDoc(documentId);
        File file = fileRepository.findByDocument(document);
        file.setDelete(true);
        fileRepository.save(file);
    }

    public String getPath(Long documentId) {
        Document document = whiteboardService.findDoc(documentId);
        File file = fileRepository.findByDocument(document);
        return file.getPath();
    }
}
