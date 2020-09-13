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
@XmlRootElement(name = "memberAnnouncement")
@XmlAccessorType(XmlAccessType.FIELD)
public class MemberAnnouncement {
    private String preferredName;
    private String unitNo;
    private String street;
    private String town;
    private String country;
    private String issueDate;
    private String fullName;
    private String membershipId;
    private String membershipDate;
    private String accountName;
    private String accountNumber;
    private String bankName;
    private String bankBranch;
}
