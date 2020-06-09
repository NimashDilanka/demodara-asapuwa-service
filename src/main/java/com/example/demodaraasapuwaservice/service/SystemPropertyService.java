package com.example.demodaraasapuwaservice.service;

import com.example.demodaraasapuwaservice.dao.SystemPropertyEntity;
import com.example.demodaraasapuwaservice.dto.SystemPropertyDto;
import com.example.demodaraasapuwaservice.mapper.SystemPropertyMapper;
import com.example.demodaraasapuwaservice.repository.SystemPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SystemPropertyService {
    public static final String MASTER_EMAIL = "MASTER_EMAIL";
    public static final String CC_EMAILS = "CC_EMAILS";
    public static final String MID_EVAL_EXP_DATE = "MID_EVAL_EXP_DATE";
    public static final String END_EVAL_EXP_DATE = "END_EVAL_EXP_DATE";
    public static final String TRANS_DATE_TOLERANCE = "TRANS_DATE_TOLERANCE";
    private final SystemPropertyRepository systemPropertyRepository;
    private final SystemPropertyMapper mapper;

    @Autowired
    public SystemPropertyService(SystemPropertyRepository systemPropertyRepository, SystemPropertyMapper mapper) {
        this.systemPropertyRepository = systemPropertyRepository;
        this.mapper = mapper;
    }

    public ResponseEntity<List<SystemPropertyDto>> getSettings() {
        List<SystemPropertyEntity> propertyList = systemPropertyRepository.findAll();
        return ResponseEntity.ok(mapper.mapLDaoToLDto(propertyList));
    }

    public ResponseEntity<List<SystemPropertyDto>> modifySettings(List<SystemPropertyDto> resource) {
        for (SystemPropertyDto property : resource) {
            Optional<SystemPropertyEntity> byId = systemPropertyRepository.findById(property.getId());
            if (!byId.isPresent()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            SystemPropertyEntity modifiedEntity = mapper.modDtoToDao(property, byId.get());
            systemPropertyRepository.save(modifiedEntity);
        }
        return ResponseEntity.ok().build();
    }
}
