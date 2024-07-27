package org.example.oristationbackend.controller;

import org.example.oristationbackend.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@RequestMapping("/api")
@RestController
public class FileUploadController {

  @Autowired
  private S3Service s3Service;

  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      String fileUrl = s3Service.uploadFile(file);
      return ResponseEntity.ok(fileUrl);
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().body("파일 업로드에 실패했습니다.");
    }
  }
}