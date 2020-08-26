package com.example.demodaraasapuwaservice.service;

import com.example.demodaraasapuwaservice.config.FileStorageProperties;
import com.example.demodaraasapuwaservice.dto.UploadFileResponse;
import com.example.demodaraasapuwaservice.file.BankRecord;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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

    public ResponseEntity<UploadFileResponse> storeCsvFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // Normalize file name
        if (!fileName.endsWith(".csv")) {
            List<String> errors = new ArrayList<>();
            errors.add("Uploaded file is not a CSV file" + fileName);
            return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                List<String> errors = new ArrayList<>();
                errors.add("Filename contains invalid characters" + fileName);
                return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName); // Copy file to the target location
            if (Files.exists(targetLocation)) {
                return getErrorResponse("File with same name already exists in system: ", fileName + "uploading file is rejected", HttpStatus.BAD_REQUEST);
            }

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/file-management/files/download/")
                    .path(fileName)
                    .toUriString();
            UploadFileResponse response = new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
            return ResponseEntity.ok(response);
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("Uploading File failed. file name: " + fileName, ex);
            logger.error("File Details: ");
            logger.error(file.toString());

            return getErrorResponse("Uploading file failed in backend: ", fileName, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Resource> loadFileAsResource(String fileName, HttpServletRequest request) {

        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        Resource resource = null;
        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            return getErrorResponse("URL Malformed. File is not found in system: ", fileName, HttpStatus.BAD_REQUEST);
        }

        if (resource == null || !resource.exists()) {
            return getErrorResponse("File is not found in system: ", fileName, HttpStatus.BAD_REQUEST);
        }

        String contentType = null; // Try to determine file's content type
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type. file name: " + fileName);
        }
        if (contentType == null) {
            contentType = "application/octet-stream"; // Fallback to the default content type if type could not be determined
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    public ResponseEntity<List<BankRecord>> previewFile(MultipartFile file) {
        List<BankRecord> bankRecords;
        try {
            bankRecords = extractData(file.getInputStream());
            if (bankRecords == null) {
                return getErrorResponse("Reading uploaded file failed: ", file.getOriginalFilename(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("extracting input stream from the multipart file is failed", ex);
            return getErrorResponse("Reading uploaded file failed: ", file.getOriginalFilename(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(bankRecords);
    }

    private ResponseEntity getErrorResponse(String errorDescription, String originalFilename, HttpStatus internalServerError) {
        List<String> errors = new ArrayList<>();
        errors.add(errorDescription + (originalFilename != null ? "filename=" + originalFilename : ""));
        return new ResponseEntity(errors, internalServerError);
    }

//    public ResponseEntity<UploadFileResponse> uploadFile(MultipartFile file) {
//        ResponseEntity<UploadFileResponse> response = storeCsvFile(file);
//        if (response.getStatusCode() != HttpStatus.OK) {
//            return response; // error occurred
//        }
//
//        response = extractData(file, response);
//        if (response.getStatusCode() != HttpStatus.OK) {
//            return response; // error occurred
//        }
//        return response;
//    }

    private List<BankRecord> extractData(InputStream inputStream) {
        CsvToBean<BankRecord> csvToBean = new CsvToBeanBuilder<BankRecord>(new InputStreamReader(inputStream))
                .withSeparator(',')
                .withType(BankRecord.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .withFilter(strings -> {
                    return strings != null && strings.length != 0
                            && !strings[0].trim().isEmpty() // remove if TxnDate is empty
                            && !strings[3].trim().isEmpty() // remove if CR is empty
                            && !strings[1].trim().isEmpty(); // remove if Description is empty
                })
                .withVerifier(bankRecord -> bankRecord.getTxnDate() != null &&
                        bankRecord.getDescription() != null &&
                        bankRecord.getCr() != 0)
                .build();
        try {
            return csvToBean.parse();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            logger.error("Parsing File into beans failed", e);
            return null;
        }
    }

}


