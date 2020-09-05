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
    private String preferredName;
    private String fullName;
    private Integer amount;
    private String description;
    private Date transactionDate;
    private Date dob;
    private Date membershipDate;
    private String tpNo;
    private String email;
    private Date addedDate;
    private Date lastModifyDate;
    private AddressDto address;
}
