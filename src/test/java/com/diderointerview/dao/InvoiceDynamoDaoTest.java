package com.diderointerview.dao;

import com.diderointerview.dao.impl.InvoiceDynamoDao;
import com.diderointerview.exception.PersistenceException;
import com.diderointerview.model.InvoiceData;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class InvoiceDynamoDaoTest {
    InvoiceDynamoDao invoiceDynamoDao = new InvoiceDynamoDao();

    @Test
    public void testPersistInvoices() throws PersistenceException {
        List<InvoiceData> invoiceDataList = Collections
                .singletonList(new InvoiceData("SK12248", "24/10/10", "Seller", "$454356"));
        invoiceDynamoDao.persistInvoices(invoiceDataList);
    }

    @Test
    public void testFetchInvoices() throws PersistenceException {
        for (InvoiceData invoiceData : invoiceDynamoDao.fetchInvoiceData()) {
            System.out.println("Invoice: " + invoiceData);
        }
    }
}
