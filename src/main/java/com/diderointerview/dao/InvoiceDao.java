package com.diderointerview.dao;

import com.diderointerview.exception.PersistenceException;
import com.diderointerview.model.InvoiceData;

import java.util.List;

public interface InvoiceDao {
    void persistInvoices(List<InvoiceData> invoiceDataList) throws PersistenceException;

    List<InvoiceData> fetchInvoiceData() throws PersistenceException;
}
