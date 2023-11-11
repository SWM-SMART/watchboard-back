package com.smart.watchboard.common.support;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.smart.watchboard.domain.File;
import com.smart.watchboard.domain.Note;
import com.smart.watchboard.dto.FileDto;
import com.smart.watchboard.dto.S3Dto;
import com.smart.watchboard.service.FileService;
import com.smart.watchboard.service.NoteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AwsS3Uploader {
    private static final String S3_BUCKET_DIRECTORY_NAME = "file";
    private final AmazonS3Client amazonS3Client;
    private final FileService fileService;
    private final NoteService noteService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(S3Dto s3Dto) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(s3Dto.getFile().getContentType());
        objectMetadata.setContentLength(s3Dto.getFile().getSize());

        String directoryName = s3Dto.getFile().getContentType();
        String fileName = directoryName + "/" + s3Dto.getDocumentId() + "." + s3Dto.getFile().getOriginalFilename();

        try (InputStream inputStream = s3Dto.getFile().getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            String path = amazonS3Client.getResourceUrl(bucket, fileName);
            FileDto fileDto = new FileDto(s3Dto.getFile(), path, s3Dto.getFile().getContentType(), s3Dto.getDocumentId());
            File file = fileService.findFileByDocument(s3Dto.getDocumentId());
            if (file == null) {
                fileService.createFile(fileDto);
            } else if (file != null) {
                fileService.updateFile(fileDto);
            }
        } catch (IOException e) {
            log.error("S3 파일 업로드에 실패했습니다. {}", e.getMessage());
            throw new IllegalStateException("S3 파일 업로드에 실패했습니다.");
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public String uploadTextPdfFile(S3Dto s3Dto) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(s3Dto.getFile().getContentType());
        objectMetadata.setContentLength(s3Dto.getFile().getSize());

        String directoryName = s3Dto.getFile().getContentType();
        String fileName = directoryName + "/" + s3Dto.getDocumentId() + "." + s3Dto.getFile().getOriginalFilename();

        try (InputStream inputStream = s3Dto.getFile().getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            String path = amazonS3Client.getResourceUrl(bucket, fileName);
            FileDto fileDto = new FileDto(s3Dto.getFile(), path, s3Dto.getFile().getContentType(), s3Dto.getDocumentId());
            Note note = noteService.findByDocument(s3Dto.getDocumentId());
            if (note == null) {
                noteService.createNote(fileDto);
            } else if (note != null) {
                noteService.updateNote(fileDto);
            }
        } catch (IOException e) {
            log.error("S3 파일 업로드에 실패했습니다. {}", e.getMessage());
            throw new IllegalStateException("S3 파일 업로드에 실패했습니다.");
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public byte[] getFileContent(Long fileId) throws IOException {
        String objectKey = fileService.findFile(fileId).get().getObjectKey();
        S3Object s3Object = amazonS3Client.getObject(bucket, objectKey);
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

        return objectInputStream.readAllBytes();
    }

    public void deleteFile(String filePath) {
        try{
            try {
                amazonS3Client.deleteObject(bucket, filePath);
            } catch (AmazonServiceException e) {
                log.info(e.getErrorMessage());
            }

        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
        log.info("[S3Uploader] : S3에 있는 파일 삭제");
    }


}
