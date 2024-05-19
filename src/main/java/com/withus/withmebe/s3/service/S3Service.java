package com.withus.withmebe.s3.service;



import static com.withus.withmebe.common.exception.ExceptionCode.FAIL_TO_REQUEST_OPEN_API;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.s3.dto.S3Dto;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class S3Service {
  @Value("${cloud.aws.s3.bucketName}")
  private String bucketName;

  private final AmazonS3Client amazonS3Client;

  private final Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  public S3Dto uploadFile(MultipartFile file) {
    String fileName = generateFileName(file);

    ObjectMetadata objectMetadata = generateObjectMetadataByMultipartFile(file);

    try (InputStream inputStream = file.getInputStream()) {
      amazonS3Client.putObject(bucketName, fileName, inputStream, objectMetadata);
      return new S3Dto(amazonS3Client.getUrl(bucketName, fileName).toString());
    } catch (IOException e) {
      log.error("IOException is occurred. ", e);
      throw new CustomException(FAIL_TO_REQUEST_OPEN_API);
    }
  }

  public List<S3Dto> uploadFiles(List<MultipartFile> files) {
    List<CompletableFuture<S3Dto>> futures = files.stream()
        .map(file -> CompletableFuture.supplyAsync(() -> uploadFile(file), executor))
        .toList();

    // Wait for all futures to complete
    return futures.stream()
        .map(CompletableFuture::join)
        .toList();
  }

  public void deleteFile(String fileUrl) {
    try {
      String fileName = extractFileNameFromUrl(fileUrl);
      amazonS3Client.deleteObject(bucketName, fileName);
      log.info("File deleted successfully: " + fileName);
    } catch (Exception e) {
      log.error("Error deleting file: " + fileUrl, e);
      throw new CustomException(FAIL_TO_REQUEST_OPEN_API);
    }
  }

  @NotNull
  private static ObjectMetadata generateObjectMetadataByMultipartFile(MultipartFile file) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(file.getContentType());
    objectMetadata.setContentLength(file.getSize());
    return objectMetadata;
  }

  @NotNull
  private static String generateFileName(MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
    String extension = StringUtils.getFilenameExtension(originalFilename); // 파일 확장자
    return UUID.randomUUID() + "." + extension;
  }

  @NotNull
  private String extractFileNameFromUrl(String fileUrl) {
    String[] urlParts = fileUrl.split("/");
    return urlParts[urlParts.length - 1];
  }
}
