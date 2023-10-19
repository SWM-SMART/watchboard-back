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

}
