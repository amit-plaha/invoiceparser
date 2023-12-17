package com.diderointerview.controller;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class InvoiceControllerTest {

    InvoiceController invoiceController = new InvoiceController();
    @Test
    public void testProcessInventories() {
        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        invoiceController.processInvoices(absolutePath);
    }
}
