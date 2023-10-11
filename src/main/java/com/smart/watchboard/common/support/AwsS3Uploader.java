package com.smart.watchboard.common.support;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.smart.watchboard.dto.FileDto;
import com.smart.watchboard.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Uploader {
    private static final String S3_BUCKET_DIRECTORY_NAME = "file";
    private final AmazonS3Client amazonS3Client;
    private final FileService fileService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile multipartFile, Long documentId, Long fileId) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String fileName = S3_BUCKET_DIRECTORY_NAME + "/" + documentId + "." + multipartFile.getOriginalFilename();

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            String path = amazonS3Client.getResourceUrl(bucket, fileName);
            FileDto fileDto = new FileDto(multipartFile, path, documentId);
            if (fileId == null) {
                fileService.createFile(fileDto);
            } else if (fileId != null) {
                fileService.updateFile(fileDto, fileId);
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
