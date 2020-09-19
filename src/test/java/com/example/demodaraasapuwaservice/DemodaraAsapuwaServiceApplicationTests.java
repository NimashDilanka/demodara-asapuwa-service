package com.example.demodaraasapuwaservice;

import com.example.demodaraasapuwaservice.controller.SettingsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemodaraAsapuwaServiceApplicationTests {
    @Autowired
    SettingsController seasonController;

    @Test
    void contextLoads() {
        assertThat(seasonController).isNotNull();
    }

}
