package com.example.demodaraasapuwaservice.controller;

import com.example.demodaraasapuwaservice.dto.UploadFileResponse;
import com.example.demodaraasapuwaservice.service.FileStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Api(value = "File Management")
@RequestMapping("/file-management/")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;

    @ApiOperation(value = "Upload a file", response = ResponseEntity.class)
    @PostMapping("/uploadFile")
    public ResponseEntity<UploadFileResponse> uploadFile(@RequestParam(name = "file") MultipartFile file) {
        return fileStorageService.storeFile(file);
    }
}
