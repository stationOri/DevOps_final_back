package org.example.oristationbackend.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

  @Value("${cloud.aws.credentials.accessKey}")
  private String accessKeyId;

  @Value("${cloud.aws.credentials.secretKey}")
  private String secretAccessKey;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Bean
  public AmazonS3 amazonS3Client() {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
    return AmazonS3ClientBuilder.standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .build();
  }
}