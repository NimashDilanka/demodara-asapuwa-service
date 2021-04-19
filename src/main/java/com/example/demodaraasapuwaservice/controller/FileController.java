package com.example.demodaraasapuwaservice.controller;

import com.example.demodaraasapuwaservice.dto.BankRecordDto;
import com.example.demodaraasapuwaservice.dto.TransMatchDto;
import com.example.demodaraasapuwaservice.service.FileStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "File Management")
@RequestMapping("/file-management/")
public class FileController {
    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @ApiOperation(value = "Preview a file", response = ResponseEntity.class)
    @PostMapping("/preview-files")
    public ResponseEntity<List<BankRecordDto>> previewCsvFile(@RequestParam(name = "file") MultipartFile file, @RequestParam(name = "month") String month, @RequestParam(name = "year") String year) {
        return fileStorageService.previewFile(file, month, year);
    }

    @ApiOperation(value = "Match Transactions", response = ResponseEntity.class)
    @PostMapping("/match-transactions")
    public ResponseEntity<List<BankRecordDto>> matchTransactions(@Valid @RequestBody TransMatchDto transMatchDto) {
        return fileStorageService.matchTransactions(transMatchDto.getRecords(), transMatchDto.getMembers());
    }

//    @ApiOperation(value = "Download a file", response = ResponseEntity.class)
//    @GetMapping("/files/download/{fileName:.+}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
//        return fileStorageService.loadFileAsResource(fileName, request);
//    }

    @ApiOperation(value = "Generate confirmation document for member", response = ResponseEntity.class)
    @GetMapping("files/gen-doc/{id}")
    public ResponseEntity<Resource> genMemberConfirmDoc(@PathVariable Integer id,
                                                        @RequestParam(name = "fileName") String fileName,
                                                        @RequestParam(name = "issueDate") String issueDate) {
        return fileStorageService.genMemberConfirmDoc(id, fileName, issueDate);
    }

    @GetMapping("{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename,
                                                 @RequestParam(name = "download") boolean download) {
        return fileStorageService.download(filename);
    }
}

