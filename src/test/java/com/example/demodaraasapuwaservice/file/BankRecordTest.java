package com.example.demodaraasapuwaservice.file;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.CsvToBeanFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
public class BankRecordTest {

    @Test
    public void testCsv() throws IOException, URISyntaxException {
        // create a reader
        Reader reader = Files.newBufferedReader(Paths.get(
                getClass().getClassLoader().getSystemResource("SampleInput_2_original.csv").toURI()));
        // create csv bean reader
        CsvToBean<BankRecord> csvToBean = new CsvToBeanBuilder<BankRecord>(reader)
                .withSeparator(',')
                .withType(BankRecord.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .withFilter(new CsvToBeanFilter() {
                    @Override
                    public boolean allowLine(String[] strings) {
                        return strings != null && strings.length != 0
                                && !strings[0].trim().isEmpty() // remove if TxnDate is empty
                                && !strings[3].trim().isEmpty() // remove if CR is empty
                                && !strings[1].trim().isEmpty(); // remove if Description is empty
                    }
                })
                .build();

        // iterate through users
        for (BankRecord tran : csvToBean) {
            System.out.println("Txn Date: " + tran.getTxnDate());
            System.out.println("Desc: " + tran.getDescription());
            System.out.println("CR: " + tran.getCr());
        }

        // close the reader
        reader.close();
    }

}
