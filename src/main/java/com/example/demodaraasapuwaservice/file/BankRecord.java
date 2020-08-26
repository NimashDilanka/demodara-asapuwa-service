package com.example.demodaraasapuwaservice.file;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BankRecord {
    @CsvDate(value = "dd/MM/yyyy")
    @CsvBindByName(column = "Txn Date")
    private Date txnDate;
    @CsvBindByName(column = "Description")
    private String description;
    @CsvBindByName(column = "CR", locale = "en")
    private double cr;
}
