package com.withus.withmebe.gathering.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImgService {

  private final AmazonS3Client s3Client;

  @Value("${cloud.aws.s3.bucketName}")
  private String bucket;

  public String updateImageToS3(MultipartFile image) throws IOException {
    String originalFileName = image.getOriginalFilename();
    String fileName = changeFileName(originalFileName);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(image.getContentType());
    metadata.setContentLength(image.getSize());

    s3Client.putObject(bucket, fileName, image.getInputStream(), metadata);

    return s3Client.getUrl(bucket, fileName).toString();
  }

  private String changeFileName(String originalFileName) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    return originalFileName + "_" + LocalDateTime.now().format(formatter);
  }
}
