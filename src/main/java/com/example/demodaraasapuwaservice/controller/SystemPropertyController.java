package com.example.demodaraasapuwaservice.controller;

import com.example.demodaraasapuwaservice.dto.SystemPropertyDto;
import com.example.demodaraasapuwaservice.service.SystemPropertyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "System Property Management")
@RequestMapping("/setting-property-management/")
public class SystemPropertyController {
    private final SystemPropertyService systemPropertyService;

    @Autowired
    public SystemPropertyController(SystemPropertyService systemPropertyService) {
        this.systemPropertyService = systemPropertyService;
    }

    @ApiOperation(value = "Retrieve all settings", response = ResponseEntity.class)
    @GetMapping("settings")
    public ResponseEntity<List<SystemPropertyDto>> getSettings() {
        return systemPropertyService.getSettings();
    }

    @ApiOperation(value = "Modify settings", response = ResponseEntity.class)
    @PutMapping("settings")
    public ResponseEntity modifySettings(@Valid @RequestBody List<SystemPropertyDto> resource) {
        return systemPropertyService.modifySettings(resource);
    }
}
