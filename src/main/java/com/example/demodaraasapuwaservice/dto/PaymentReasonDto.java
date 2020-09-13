package com.example.demodaraasapuwaservice.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReasonDto {
    private Integer id;
    private String code;
    private String value;
    private Date addedDate;
    private Date lastModifyDate;
}
