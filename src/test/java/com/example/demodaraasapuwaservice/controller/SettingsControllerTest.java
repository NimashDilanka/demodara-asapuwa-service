package com.example.demodaraasapuwaservice.controller;

import com.example.demodaraasapuwaservice.dto.SystemPropertyDto;
import com.example.demodaraasapuwaservice.mapper.SystemPropertyMapperImpl;
import com.example.demodaraasapuwaservice.repository.SystemPropertyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.demodaraasapuwaservice.service.SystemPropertyService.*;

@RunWith(SpringRunner.class)
@WebMvcTest()
public class SettingsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    SystemPropertyRepository repo;
    @Autowired
    SystemPropertyMapperImpl mapper;

    @Test
    public void feedData() {
        SystemPropertyDto masterEmail = new SystemPropertyDto();
        masterEmail.setCode(MASTER_EMAIL);
        masterEmail.setValue("example@email.com");
        masterEmail.setEnable(true);
        SystemPropertyDto ccEmails = new SystemPropertyDto();
        ccEmails.setCode(CC_EMAILS);
        ccEmails.setValue("kkk@email.com");
        ccEmails.setEnable(true);
        SystemPropertyDto midEvalDate = new SystemPropertyDto();
        midEvalDate.setCode(MID_EVAL_EXP_DATE);
        midEvalDate.setValue("15");
        midEvalDate.setEnable(true);
        SystemPropertyDto endEvalDate = new SystemPropertyDto();
        endEvalDate.setCode(END_EVAL_EXP_DATE);
        endEvalDate.setValue("25");
        endEvalDate.setEnable(true);
        SystemPropertyDto dateTolerance = new SystemPropertyDto();
        dateTolerance.setCode(TRANS_DATE_TOLERANCE);
        dateTolerance.setValue("3");
        dateTolerance.setEnable(true);

        repo.save(mapper.mapDtoToDao(masterEmail));
        repo.save(mapper.mapDtoToDao(ccEmails));
        repo.save(mapper.mapDtoToDao(midEvalDate));
        repo.save(mapper.mapDtoToDao(endEvalDate));
        repo.save(mapper.mapDtoToDao(dateTolerance));
    }
}
