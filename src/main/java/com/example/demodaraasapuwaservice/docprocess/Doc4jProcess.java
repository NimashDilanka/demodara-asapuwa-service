package com.example.demodaraasapuwaservice.docprocess;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Text;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Doc4jProcess {
    private static WordprocessingMLPackage getTemplate(String name) throws Docx4JException {
        WordprocessingMLPackage template = WordprocessingMLPackage.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("templates/english/" + name));
        return template;
    }

    private static List<Object> getAllElementsFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<>();
        if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch))
            result.add(obj);
        else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementsFromObject(child, toSearch));
            }
        }
        return result;
    }

    public static WordprocessingMLPackage generateDoc(String templateDocName, Map<String, String> valueMap) throws Docx4JException {
        WordprocessingMLPackage template = getTemplate(templateDocName);
        List<Object> texts = getAllElementsFromObject(template.getMainDocumentPart(), Text.class);
        for (Map.Entry<String, String> entry : valueMap.entrySet()) {
            for (Object text : texts) {
                Text textElement = (Text) text;
                if (textElement.getValue().equals(entry.getKey())) {
                    textElement.setValue(entry.getValue());
                }
            }
        }
        return template;
    }
}
