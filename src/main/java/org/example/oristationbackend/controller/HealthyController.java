package org.example.oristationbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/healthy")
public class HealthyController {
    @GetMapping
    public ResponseEntity<String> healthycheck() {
       return ResponseEntity.ok("ok");
    }
}
