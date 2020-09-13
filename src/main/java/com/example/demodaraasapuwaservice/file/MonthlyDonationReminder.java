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
@XmlRootElement(name = "monthlyDonationReminder")
@XmlAccessorType(XmlAccessType.FIELD)
public class MonthlyDonationReminder {
    private String preferredName;
    private String unitNo;
    private String street;
    private String town;
    private String country;
    private String issueDate;
    private String month;
    private String trancationDates;
    private String endDate;
}
