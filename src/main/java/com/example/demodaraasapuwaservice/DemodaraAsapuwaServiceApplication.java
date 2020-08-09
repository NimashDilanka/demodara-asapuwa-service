package com.example.demodaraasapuwaservice;

import com.example.demodaraasapuwaservice.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class DemodaraAsapuwaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemodaraAsapuwaServiceApplication.class, args);
    }

}
