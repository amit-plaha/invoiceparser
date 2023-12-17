package com.diderointerview.service.impl;

import com.diderointerview.dao.InvoiceDao;
import com.diderointerview.dao.impl.InvoiceDynamoDao;
import com.diderointerview.exception.ServiceException;
import com.diderointerview.model.InvoiceData;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class InvoiceService {

    // since we are parsing only pdf type hence we can do this, otherwise factory implementation would be
    // needed to support different parser types
    private final DocumentParser documentParser = new PDFDocumentParser();

    private final OpenAPIInvoiceHelper openAPIInvoiceHelper = new OpenAPIInvoiceHelper();

    private final InvoiceDao invoiceDao = new InvoiceDynamoDao();

    public void processInvoices(String invoiceDirectoryPath) throws ServiceException {
        try {
            List<String> textForDocuments = documentParser.extractTextForDocuments(invoiceDirectoryPath);
            List<InvoiceData> invoicesToBePersisted = new ArrayList<>();
            for (String documentText : textForDocuments) {
                Optional<InvoiceData> openAIAPIResponseStrOpt = openAPIInvoiceHelper.extractInvoiceData(documentText);
                openAIAPIResponseStrOpt.ifPresent(invoiceData -> invoicesToBePersisted.add(invoiceData));
            }
            invoiceDao.persistInvoices(invoicesToBePersisted);
        } catch (Exception exception) {
            String errorMessage = "Caught exception in processInvoices service";
            log.error(errorMessage);
            throw new ServiceException(errorMessage, exception);
        }

    }

    public void displayExistingInvoices() throws ServiceException {
        System.out.println("Displaying Invoices...");
        try {
            List<InvoiceData> invoiceDataList = invoiceDao.fetchInvoiceData();

            for (InvoiceData invoiceData : invoiceDataList) {
                String displayString = String.format("Number: %s; Seller: %s, Date: %s; Total Amount: %s",
                        invoiceData.getInvoiceNumber(), invoiceData.getSellerCompanyName(), invoiceData.getDate(), invoiceData.getTotalAmount());
                System.out.println(displayString);
            }
        } catch (Exception exception) {
            String errorMessage = "Caught exception in processInvoices service";
            log.error(errorMessage);
            throw new ServiceException(errorMessage, exception);
        }

    }

}
