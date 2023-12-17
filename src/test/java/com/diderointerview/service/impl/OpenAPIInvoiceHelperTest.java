package com.diderointerview.service.impl;

import com.diderointerview.model.InvoiceData;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenAPIInvoiceHelperTest {

    OpenAPIInvoiceHelper openAPIInvoiceHelper = new OpenAPIInvoiceHelper();
    PDFDocumentParser pdfDocumentParser = new PDFDocumentParser(); // TODO: should be mocked!

    @Test
    public void testCallOpenAIAPIWithValidDocText() throws JsonProcessingException {
        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        List<String> texts = pdfDocumentParser.extractTextForDocuments(absolutePath);
        Optional<InvoiceData> responseOpt1 = openAPIInvoiceHelper.extractInvoiceData(texts.get(0));
        Optional<InvoiceData> responseOpt2 = openAPIInvoiceHelper.extractInvoiceData(texts.get(1));
        assertTrue(responseOpt1.isPresent());
        assertTrue(responseOpt2.isPresent());
    }
}
