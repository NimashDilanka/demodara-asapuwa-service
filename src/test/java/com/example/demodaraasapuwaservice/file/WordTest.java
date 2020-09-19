package com.example.demodaraasapuwaservice.file;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringWriter;

@RunWith(SpringRunner.class)
public class WordTest {
    public static final String input_DOCX = "D:\\DemodaraAsapuwa\\SpecialDonationReceivalTemplate.docx";
    public static final String output_DOCX = "D:\\DemodaraAsapuwa\\MonthlyDonationReceivalTemplate_result.docx";

    @Test
    public void testDoc4j() throws Docx4JException, FileNotFoundException, JAXBException {
        SpecialDonationReceival xmlSample = new SpecialDonationReceival("preferName", "Unit 5", "Street Name", "my town", "my country", "25/08/2020", "Rs.5000", "Rs.2000 on 28/05/2020",
                "full name", "ID2355M");
        JAXBContext jaxbContext = JAXBContext.newInstance(SpecialDonationReceival.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(xmlSample, sw);
        String xmlContent = sw.toString();
        System.out.println(xmlContent);

        WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(input_DOCX));
        Docx4J.bind(wordMLPackage, xmlContent, Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML | Docx4J.FLAG_BIND_REMOVE_SDT);
        Docx4J.save(wordMLPackage, new File(output_DOCX), Docx4J.FLAG_NONE);
        System.out.println("Saved: " + output_DOCX);
    }
}
