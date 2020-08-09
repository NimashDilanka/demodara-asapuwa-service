package com.example.demodaraasapuwaservice.service;

import com.example.demodaraasapuwaservice.config.FileStorageProperties;
import com.example.demodaraasapuwaservice.dto.UploadFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public ResponseEntity<UploadFileResponse> storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // Normalize file name
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                List<String> errors = new ArrayList<>();
                errors.add("Filename contains invalid charactors" + fileName);
                return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName); // Copy file to the target location
            if (Files.exists(targetLocation)) {
                List<String> errors = new ArrayList<>();
                errors.add("File with same name already exists in system: " + (fileName != null ? "filename=" + fileName : "" + "uploading file is rejected"));
                return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
            }

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/files/download/")
                    .path(fileName)
                    .toUriString();
            UploadFileResponse response = new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
            return ResponseEntity.ok(response);
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("Uploading File failed. file name: " + fileName, ex);
            logger.error("File Details: ");
            logger.error(file.toString());

            List<String> errors = new ArrayList<>();
            errors.add("Uploading file failed in backend: " + (fileName != null ? "filename=" + fileName : ""));
            return new ResponseEntity(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

