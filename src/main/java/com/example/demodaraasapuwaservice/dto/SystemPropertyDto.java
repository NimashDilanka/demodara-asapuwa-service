package com.example.demodaraasapuwaservice.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SystemPropertyDto {
    private int id;
    private String code;
    private String value;
    private boolean enable;
    private Date addedDate;
    private Date lastModifyDate;
}
