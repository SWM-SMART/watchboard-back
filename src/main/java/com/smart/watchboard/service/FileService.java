package com.smart.watchboard.service;

import com.smart.watchboard.domain.Document;
import com.smart.watchboard.domain.File;
import com.smart.watchboard.dto.FileDto;
import com.smart.watchboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRepository fileRepository;
    private final WhiteboardService whiteboardService;

    public void createFile(FileDto fileDto) {
        Document document = whiteboardService.findDoc(fileDto.getDocumentId());
        File file = File.builder()
                .fileName(fileDto.getAudioFile().getOriginalFilename())
                .path(fileDto.getPath())
                .size(fileDto.getAudioFile().getSize())
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .isDelete(false)
                .document(document)
                .build();

        fileRepository.save(file);
    }

    public void updateFile(FileDto fileDto, long fileId) {
        //Document document = whiteboardService.findDoc(fileDto.getDocumentId());
        Optional<File> file = findFile(fileId);
        File updatedFile = file.get();
        updatedFile.setFileName(fileDto.getAudioFile().getOriginalFilename());
        updatedFile.setPath(fileDto.getPath());
        updatedFile.setSize(fileDto.getAudioFile().getSize());
        updatedFile.setCreatedAt(Instant.now());
        updatedFile.setModifiedAt(Instant.now());

        fileRepository.save(updatedFile);
    }

    public void deleteFile(MultipartFile multipartFile, long documentId) {
        return;
    }

    public Optional<File> findFile(long fileId) {
        Optional<File> file = fileRepository.findById(fileId);

        return file;
    }

}
