package com.example.demodaraasapuwaservice.service;

import com.example.demodaraasapuwaservice.config.FileStorageProperties;
import com.example.demodaraasapuwaservice.dao.MemberEntity;
import com.example.demodaraasapuwaservice.dao.SystemPropertyEntity;
import com.example.demodaraasapuwaservice.docprocess.Doc4jProcess;
import com.example.demodaraasapuwaservice.dto.BankRecordDto;
import com.example.demodaraasapuwaservice.dto.MatchDegree;
import com.example.demodaraasapuwaservice.dto.MemberDto;
import com.example.demodaraasapuwaservice.dto.UploadFileResponse;
import com.example.demodaraasapuwaservice.repository.MemberRepository;
import com.example.demodaraasapuwaservice.repository.SystemPropertyRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
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
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    private final String DATE_REGEX = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
    private final Path fileStorageLocation;
    private final SystemPropertyRepository systemPropertyRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties, SystemPropertyRepository systemPropertyRepository, MemberRepository memberRepository) {
        this.systemPropertyRepository = systemPropertyRepository;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.memberRepository = memberRepository;
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
                return getErrorResponse("File with same name already exists in system: ", fileName + " uploading file is rejected", HttpStatus.BAD_REQUEST);
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

    public ResponseEntity<List<BankRecordDto>> previewFile(MultipartFile file, String month, String year) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // Normalize file name
        if (!fileName.endsWith(".csv")) {
            List<String> errors = new ArrayList<>();
            errors.add("Uploaded file is not a CSV file" + fileName);
            return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
        }

        List<BankRecordDto> bankRecordDtos;
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                List<String> errors = new ArrayList<>();
                errors.add("Filename contains invalid characters" + fileName);
                return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
            }

//            Path targetLocation = this.fileStorageLocation.resolve(fileName);
//            if (Files.exists(targetLocation)) {
//                return getErrorResponse("File with same name already exists in system: ", fileName + " uploading file is rejected", HttpStatus.BAD_REQUEST);
//            }
            bankRecordDtos = extractData(file.getInputStream(), month, year);
            if (bankRecordDtos == null) {
                return getErrorResponse("Reading uploaded file failed: ", file.getOriginalFilename(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("Extracting input stream from the multipart file is failed", ex);
            return getErrorResponse("Reading uploaded file failed: ", file.getOriginalFilename(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(bankRecordDtos);
    }

    public ResponseEntity<Resource> genMemberConfirmDoc(Integer id, String newFileName, String issueDate) {
        Optional<MemberEntity> byId = memberRepository.findById(id);
        if (!byId.isPresent()) {
            List<String> errors = new ArrayList<>();
            errors.add("Membership Confirmation Report Generation Request Rejected. Member with id: " + id + " not available on system.");
            return new ResponseEntity(errors, HttpStatus.NOT_FOUND);
        }
        MemberEntity m = byId.get();

        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("PREFFERED_NAME", m.getPreferredName());
        valueMap.put("UNIT_NO", m.getAddress().getUnitNo());
        valueMap.put("STREET", m.getAddress().getStreet());
        valueMap.put("TOWN", m.getAddress().getTown());
        valueMap.put("COUNTRY", m.getAddress().getCountry());
        valueMap.put("ISSUE_DATE", issueDate);
        valueMap.put("FULL_NAME", m.getFullName());
        valueMap.put("MEMBERSHIP_ID", m.getMembershipId());
        valueMap.put("MEMBERSHIP_DATE", m.getMembershipDate().toString());
        valueMap.put("ACC_NAME", systemPropertyRepository.getByCode(SystemPropertyService.ACCOUNT_NAME).get().getValue());
        valueMap.put("ACC_NUMBER", systemPropertyRepository.getByCode(SystemPropertyService.ACCOUNT_NUMBER).get().getValue());
        valueMap.put("BANK_NAME", systemPropertyRepository.getByCode(SystemPropertyService.BANK_NAME).get().getValue());
        valueMap.put("BANK_BRANCH", systemPropertyRepository.getByCode(SystemPropertyService.BANK_BRANCH).get().getValue());

        Resource resource;
        Path targetLocation = this.fileStorageLocation.resolve(newFileName);
        try {
            WordprocessingMLPackage template = Doc4jProcess.generateDoc("AnnouncementTemp.docx", valueMap);
            File outDoc = new File(targetLocation.toUri());
            Docx4J.save(template, outDoc); //Save internally
        } catch (Docx4JException e) {
            logger.error("Membership Confirmation Report Generation Request Rejected. Error in doc file generation.", e);
            e.printStackTrace();
            List<String> errors = new ArrayList<>();
            errors.add("Membership Confirmation Report Generation Request Rejected. Error in doc file generation.");
            return new ResponseEntity(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            resource = new UrlResource(targetLocation.toUri());
            if (resource == null || !resource.exists()) {
                return getErrorResponse("Membership Confirmation Report Generation Request Rejected. File is not found in system: ", targetLocation.getFileName().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(targetLocation))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return getErrorResponse("URL Malformed. File is not found in system: ", targetLocation.getFileName().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<Resource> download(String filename) {
        try {
            Path file = fileStorageLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                Path path = resource.getFile().toPath();

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }

    }

    private ResponseEntity getErrorResponse(String errorDescription, String originalFilename, HttpStatus internalServerError) {
        List<String> errors = new ArrayList<>();
        errors.add(errorDescription + (originalFilename != null ? "filename=" + originalFilename : ""));
        return new ResponseEntity(errors, internalServerError);
    }

    private List<BankRecordDto> extractData(InputStream inputStream, String month, String year) {
        CsvToBean<BankRecordDto> csvToBean = new CsvToBeanBuilder<BankRecordDto>(new InputStreamReader(inputStream))
                .withSeparator(',')
                .withType(BankRecordDto.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .withFilter(strings -> {
                    return strings != null && strings.length != 0
                            && !strings[0].trim().isEmpty() // remove if TxnDate is empty
                            && Pattern.matches(DATE_REGEX, strings[0].trim()) // TxnDate matches dd/mm/yyyy format
                            && Integer.parseInt(year) == Integer.parseInt(strings[0].trim().split("/")[2]) // remove if TxnDate year  is unmatched
                            && Integer.parseInt(month) == Integer.parseInt(strings[0].trim().split("/")[1]) // remove if TxnDate month  is unmatched
                            && !strings[3].trim().isEmpty() // remove if CR is empty
                            && !strings[1].trim().isEmpty(); // remove if Description is empty
                })
                .withVerifier(bankRecordDto -> bankRecordDto.getTxnDate() != null &&
                        bankRecordDto.getDescription() != null &&
                        bankRecordDto.getCr() != 0)
                .build();
        try {
            return csvToBean.parse();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            logger.error("Parsing File into beans failed", e);
            return null;
        }
    }

    public ResponseEntity<List<BankRecordDto>> matchTransactions(List<BankRecordDto> records, List<MemberDto> members) {
        ArrayList<BankRecordDto> matchedRecords = new ArrayList<>();

        //just match looking at description
        Iterator<BankRecordDto> perfrctItr = records.iterator();
        while (perfrctItr.hasNext()) {
            BankRecordDto record = perfrctItr.next();
            Optional<MemberDto> matchedMember = members.stream()
                    .filter(m -> m.getDescription().trim().equalsIgnoreCase(record.getDescription()) &&
                            m.getAmount().equals(record.getCr())
                    )
                    .findFirst();
            if (matchedMember.isPresent()) {
                record.setMemberId(matchedMember.get().getId());
                record.setMatchDegree(MatchDegree.PERFECT);
                matchedRecords.add(record);
                perfrctItr.remove();
            }
        }

        //try to match using date and amount
        Optional<SystemPropertyEntity> tolerance = systemPropertyRepository.getByCode(SystemPropertyService.TRANS_DATE_TOLERANCE);
        int tolaranceRange = tolerance.isPresent() ? Integer.parseInt(tolerance.get().getValue()) : 0;
        Iterator<BankRecordDto> doubtItr = records.iterator();
        while (doubtItr.hasNext()) {
            BankRecordDto record = doubtItr.next();
            Optional<MemberDto> matchedMember = members.stream().filter(m -> memberMatched(record, m, tolaranceRange)).findFirst();
            if (matchedMember.isPresent()) {
                record.setMemberId(matchedMember.get().getId());
                record.setMatchDegree(MatchDegree.DOUBTFUL);
                matchedRecords.add(record);
                doubtItr.remove();
            }
        }

        //update no matching entries
        records.forEach(r -> {
            r.setMemberId(0);
            r.setMatchDegree(MatchDegree.NO_MATCH);
            matchedRecords.add(r);
        });

        return ResponseEntity.ok(matchedRecords);
    }

    private boolean memberMatched(BankRecordDto record, MemberDto m, int toleranceRange) {
        boolean amountMatched = m.getAmount().equals(record.getCr());
        Calendar c = Calendar.getInstance();
        c.setTime(m.getTransactionDate());
        int scheduledDate = c.get(Calendar.DAY_OF_MONTH);

        c.setTime(record.getTxnDate());
        int transactionDate = c.get(Calendar.DAY_OF_MONTH);

        int expiryDate = scheduledDate + toleranceRange > 31 ? 31 : scheduledDate + toleranceRange;

        boolean withinExpiryRange = transactionDate >= scheduledDate && transactionDate <= expiryDate;
        return amountMatched && withinExpiryRange;
    }
}


