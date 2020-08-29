package com.example.demodaraasapuwaservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransMatchDto {
    private List<BankRecordDto> records;
    private List<MemberDto> members;

}
