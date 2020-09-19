package com.example.demodaraasapuwaservice.controller;

import com.example.demodaraasapuwaservice.dto.SettingResponse;
import com.example.demodaraasapuwaservice.service.SystemPropertyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "Settings Management")
@RequestMapping("/settings-management/")
public class SettingsController {
    private final SystemPropertyService systemPropertyService;

    @Autowired
    public SettingsController(SystemPropertyService systemPropertyService) {
        this.systemPropertyService = systemPropertyService;
    }

    @ApiOperation(value = "Retrieve all settings", response = ResponseEntity.class)
    @GetMapping("settings")
    public ResponseEntity<SettingResponse> getSettings() {
        return systemPropertyService.getSettings();
    }

    @ApiOperation(value = "Modify settings", response = ResponseEntity.class)
    @PutMapping("settings")
    public ResponseEntity modifySettings(@Valid @RequestBody SettingResponse resource) {
        return systemPropertyService.modifySettings(resource);
    }
}
