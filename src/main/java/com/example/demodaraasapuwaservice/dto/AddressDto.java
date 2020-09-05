package com.example.demodaraasapuwaservice.dto;

import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Integer id;
    private String unitNo;
    private String Street;
    private String town;
    private String country;
}
