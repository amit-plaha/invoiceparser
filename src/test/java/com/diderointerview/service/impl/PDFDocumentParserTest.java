package com.diderointerview.service.impl;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PDFDocumentParserTest {

    PDFDocumentParser pdfDocumentParser = new PDFDocumentParser();
    @Test
    public void testParsePDFDocumentWithValidPDFDocument() {
        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        List<String> texts = pdfDocumentParser.extractTextForDocuments(absolutePath);
        assertNotNull(texts);
        assertTrue(texts.size() == 2);
    }
}
