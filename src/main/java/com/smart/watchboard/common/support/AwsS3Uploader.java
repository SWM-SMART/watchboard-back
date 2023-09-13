package com.smart.watchboard.common.support;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
    private static final String S3_BUCKET_DIRECTORY_NAME = "static";
    private final AmazonS3Client amazonS3Client;
    private final FileService fileService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile multipartFile, Long documentId, Long fileId) {
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
                // 새로운 파일로 업데이트할 때 기존 파일 삭제할지 고민 필요
                fileService.updateFile(fileDto, fileId);
            }
        } catch (IOException e) {
            log.error("S3 파일 업로드에 실패했습니다. {}", e.getMessage());
            throw new IllegalStateException("S3 파일 업로드에 실패했습니다.");
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }


}
