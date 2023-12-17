package com.diderointerview.controller;

import com.diderointerview.service.impl.InvoiceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvoiceController {

    private final InvoiceService invoiceService = new InvoiceService();
    public void processInvoices(String invoiceDirectoryPath) {

        try {
            invoiceService.processInvoices(invoiceDirectoryPath);
            invoiceService.displayExistingInvoices();
        } catch (Exception exception) {
            log.error("Caught exception in controller while processing: {}", exception);
        }
    }

    public static void main(String args[]) {
        if (args != null && args.length != 0) {
            String directoryPath = args[0];
            InvoiceController invoiceController = new InvoiceController();
            invoiceController.processInvoices(directoryPath);
        } else {
            System.out.println("Directory path for invoices not specified");
        }
    }
}
