package com.example.demodaraasapuwaservice.file;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "specialDonationReceival")
@XmlAccessorType(XmlAccessType.FIELD)
public class SpecialDonationReceival {
    private String preferredName;
    private String unitNo;
    private String street;
    private String town;
    private String country;
    private String issueDate;
    private String totalAmount;
    private String trancationsAndDates;
    private String fullName;
    private String membershipId;
}
