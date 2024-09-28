package com.cabin.plat.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.cabin.plat.global.exception.RestApiException;
import com.cabin.plat.global.exception.errorCode.S3ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3FileComponent {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(String category, MultipartFile multipartFile) {
        // 파일명
        String fileName = createFileName(category, Objects.requireNonNull(multipartFile.getOriginalFilename()));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        // 파일 타입 확인
        boolean isImage = multipartFile.getContentType() != null && multipartFile.getContentType().startsWith("image");

        // 바이트 배열로 파일 내용 읽기
        byte[] bytes;
        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            // 파일 업로드 실패 시 에러 코드 분기
            if (isImage) {
                throw new RestApiException(S3ErrorCode.FALIED_READ_IMAGE);
            } else {
                throw new RestApiException(S3ErrorCode.FALIED_READ_FILE);
            }
        }

        // Content-Length 설정
        objectMetadata.setContentLength(bytes.length);

        // ByteArrayInputStream 생성
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        // S3에 업로드
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, byteArrayInputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonS3Exception e) {
            // S3 업로드 실패 시 에러 코드 분기
            if (isImage) {
                System.out.println("TEST");
                System.out.println(e.getMessage());
                System.out.println(isImage);
                throw new RestApiException(S3ErrorCode.FAILED_UPLOAD_S3_IMAGE);
            } else {
                throw new RestApiException(S3ErrorCode.FAILED_UPLOAD_S3_FILE);
            }
        }

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * 파일명 생성
     * @param category
     * @param originalFileName
     * @return 작명된 파일 이름
     */
    public String createFileName(String category, String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(".");
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String random = String.valueOf(UUID.randomUUID());

        return category + "/" + fileName + "_" + random + fileExtension;
    }

    /**
     * 이미지 삭제
     * @param fileUrl
     */
    public void deleteFile(String fileUrl) {
        String[] deleteUrl = fileUrl.split("/", 4);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, deleteUrl[3]));
    }
}
