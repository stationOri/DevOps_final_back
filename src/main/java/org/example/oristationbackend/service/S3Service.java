package org.example.oristationbackend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

  private final AmazonS3 amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  @Autowired
  public S3Service(AmazonS3 amazonS3Client) {
    this.amazonS3Client = amazonS3Client;
  }

  public String uploadFile(MultipartFile file) throws IOException {
    String fileName = generateFileName(file);
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(file.getContentType());
    metadata.setContentLength(file.getSize());

    amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));

    return amazonS3Client.getUrl(bucketName, fileName).toString();
  }

  private String generateFileName(MultipartFile file) {
    return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
  }
}