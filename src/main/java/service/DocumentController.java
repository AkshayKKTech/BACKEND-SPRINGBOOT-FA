package com.firstamerican.portal.service;

import com.firstamerican.portal.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/{orderId}/upload")
    public ResponseEntity<?> uploadFile(
            @PathVariable String orderId,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "File cannot be empty."));
        }

        try {
            String s3Url = documentService.uploadDocumentToS3(orderId, file);
            return ResponseEntity.ok(Map.of(
                    "message", "Document successfully stored in AWS S3 Secure Vault!",
                    "orderId", orderId,
                    "s3Url", s3Url
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "S3 Upload Failed: " + e.getMessage()));
        }
    }
}