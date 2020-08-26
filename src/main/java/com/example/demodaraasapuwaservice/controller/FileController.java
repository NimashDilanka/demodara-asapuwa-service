package com.example.demodaraasapuwaservice.controller;

import com.example.demodaraasapuwaservice.file.BankRecord;
import com.example.demodaraasapuwaservice.service.FileStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(value = "File Management")
@RequestMapping("/file-management/")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;

    @ApiOperation(value = "Preview a file", response = ResponseEntity.class)
    @PostMapping("/preview-files")
    public ResponseEntity<List<BankRecord>> previewCsvFile(@RequestParam(name = "file") MultipartFile file) {
        return fileStorageService.previewFile(file);
    }

    @ApiOperation(value = "Download a file", response = ResponseEntity.class)
    @GetMapping("/files/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        return fileStorageService.loadFileAsResource(fileName, request);
    }
}

