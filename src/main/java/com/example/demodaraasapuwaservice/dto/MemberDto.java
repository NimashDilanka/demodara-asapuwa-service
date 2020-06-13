package com.example.demodaraasapuwaservice.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Integer id;
    private String name;
    private int amount;
    private String description;
    private Date transactionDate;
    private String tpNo;
    private String email;
    private Date addedDate;
    private Date lastModifyDate;
}
