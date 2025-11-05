package com.demoapp.demoapp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demoapp.demoapp.model.response.S3FileDTO;
import com.demoapp.demoapp.service.AwsService;

@RestController
@RequestMapping("/api/aws")
public class AwsController {

    private final AwsService awsService;

    public AwsController(AwsService awsService) {
        this.awsService = awsService;
    }

    @PostMapping("s3/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            String objectKey = file.getOriginalFilename();
            awsService.uploadFile(objectKey, file);
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        }
    }

    @GetMapping("s3/download/{fileName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable String fileName) {
        try {
            var fileData = awsService.readFile(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileData.getContentType()))
                    .header("Content-Disposition", "inline; filename=\"" + fileData.getFileName() + "\"")
                    .body(new ByteArrayResource(fileData.getContent()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("s3/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<S3FileDTO>> getFiles() {
        var fileData = awsService.getFileList();
        return ResponseEntity.ok(fileData);
    }

    @DeleteMapping("s3/delete/{fileName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        try {
            awsService.deleteFile(fileName);
            return ResponseEntity.ok("File deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting file: " + e.getMessage());
        }
    }
}
