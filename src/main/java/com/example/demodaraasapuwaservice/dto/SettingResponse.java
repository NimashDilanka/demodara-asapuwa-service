package com.example.demodaraasapuwaservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SettingResponse {
    List<SystemPropertyDto> systemPropertyList;
    List<PaymentReasonDto> paymentReasonList;
}
