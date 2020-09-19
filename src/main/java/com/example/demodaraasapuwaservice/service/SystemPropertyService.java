package com.example.demodaraasapuwaservice.service;

import com.example.demodaraasapuwaservice.dao.PaymentReasonEntity;
import com.example.demodaraasapuwaservice.dao.SystemPropertyEntity;
import com.example.demodaraasapuwaservice.dto.PaymentReasonDto;
import com.example.demodaraasapuwaservice.dto.SettingResponse;
import com.example.demodaraasapuwaservice.dto.SystemPropertyDto;
import com.example.demodaraasapuwaservice.mapper.PaymentReasonMapper;
import com.example.demodaraasapuwaservice.mapper.SystemPropertyMapper;
import com.example.demodaraasapuwaservice.repository.PaymentReasonRepository;
import com.example.demodaraasapuwaservice.repository.SystemPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SystemPropertyService {
    public static final String MASTER_EMAIL = "MASTER_EMAIL";
    public static final String CC_EMAILS = "CC_EMAILS";
    public static final String MID_EVAL_EXP_DATE = "MID_EVAL_EXP_DATE";
    public static final String END_EVAL_EXP_DATE = "END_EVAL_EXP_DATE";
    public static final String TRANS_DATE_TOLERANCE = "TRANS_DATE_TOLERANCE";
    private final SystemPropertyRepository systemPropertyRepository;
    private final PaymentReasonRepository paymentReasonRepository;
    private final SystemPropertyMapper sysMapper;
    private final PaymentReasonMapper payMapper;

    @Autowired
    public SystemPropertyService(SystemPropertyRepository systemPropertyRepository, PaymentReasonRepository paymentReasonRepository, SystemPropertyMapper sysMapper, PaymentReasonMapper payMapper) {
        this.systemPropertyRepository = systemPropertyRepository;
        this.paymentReasonRepository = paymentReasonRepository;
        this.sysMapper = sysMapper;
        this.payMapper = payMapper;
    }

    public ResponseEntity<SettingResponse> getSettings() {
        SettingResponse res = new SettingResponse();
        List<SystemPropertyEntity> propertyList = systemPropertyRepository.findAll();
        List<PaymentReasonEntity> paymentReasons = paymentReasonRepository.findAll();
        res.setPaymentReasonList(payMapper.mapLDaoToLDto(paymentReasons));
        res.setSystemPropertyList(sysMapper.mapLDaoToLDto(propertyList));
        return ResponseEntity.ok(res);
    }

    public ResponseEntity<SettingResponse> modifySettings(SettingResponse resource) {
        for (SystemPropertyDto property : resource.getSystemPropertyList()) {
            Optional<SystemPropertyEntity> byId = systemPropertyRepository.findById(property.getId());
            if (!byId.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            SystemPropertyEntity modifiedEntity = sysMapper.modDtoToDao(property, byId.get());
            systemPropertyRepository.save(modifiedEntity);
        }

        //remove invalid Payment reasons
        List<Integer> ids = resource.getPaymentReasonList().stream().map(p -> p.getId()).collect(Collectors.toList());
        List<PaymentReasonEntity> resons = paymentReasonRepository.findAll();
        List<Integer> deletingIds = resons.stream().filter(r -> !ids.contains(r.getId())).map(r -> r.getId()).collect(Collectors.toList());
        deletingIds.forEach(id -> paymentReasonRepository.deleteById(id));

        for (PaymentReasonDto payment : resource.getPaymentReasonList()) {
            Optional<PaymentReasonEntity> byId1 = paymentReasonRepository.findById(payment.getId());
            if (byId1.isPresent()) {
                PaymentReasonEntity modifiedPayment = payMapper.modDtoToDao(payment, byId1.get());
                paymentReasonRepository.save(modifiedPayment);
            } else {
                PaymentReasonEntity newPayment = payMapper.mapDtoToDao(payment);
                paymentReasonRepository.save(newPayment);
            }
        }

        return ResponseEntity.ok().build();
    }
}
