package com.diderointerview.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PDFDocumentParser implements DocumentParser {

    @Override
    public List<String> extractTextForDocuments(String documentFolderPath) {
        File directoryPath = new File(documentFolderPath);
        //List of all files and directories
        File filesList[] = directoryPath.listFiles();
        List<String> textForDocuments = new ArrayList<>();
        for (File file : filesList) {
            Optional<String> textOpt = parsePDFDocument(file.getAbsolutePath());
            // skip malformatted files
            textOpt.ifPresentOrElse(
                    text -> {
                        textForDocuments.add(text);
                        log.debug("Text from pdf: {}", text);
                    },
                    () -> log.warn("File couldn't be processed: {}", file.getAbsolutePath()));
        }
        return textForDocuments;
    }

    private Optional<String> parsePDFDocument(String pdfLocationURL) {

        String extractedText = null;
        try {
            File pdfFile = new File(pdfLocationURL);
            PDDocument document  = PDDocument.load(pdfFile);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();

            extractedText = pdfTextStripper.getText(document);
            log.debug("Extracted text: {}", extractedText);
            document.close();
        }
        catch (IOException ioException) {
            log.error("Can't parse pdf file at location: {}, failed with exception: {}", pdfLocationURL, ioException.getStackTrace());
        }

        return Optional.ofNullable(extractedText);
    }


}
