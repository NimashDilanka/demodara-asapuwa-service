package com.example.demodaraasapuwaservice.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BankRecordDto {
    @CsvDate(value = "dd/MM/yyyy")
    @CsvBindByName(column = "Txn Date")
    private Date txnDate;
    @CsvBindByName(column = "Description")
    private String description;
    @CsvBindByName(column = "CR", locale = "en")
    private Integer cr;
    private Integer memberId;
    private MatchDegree matchDegree;

    public BankRecordDto(Date txnDate, String description, Integer cr) {
        this.txnDate = txnDate;
        this.description = description;
        this.cr = cr;
    }
}
