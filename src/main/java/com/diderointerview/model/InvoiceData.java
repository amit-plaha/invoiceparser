package com.diderointerview.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InvoiceData {

    /**
     * Uniquely identifies an invoice
     */
    @JsonProperty("Invoice Number")
    private String invoiceNumber;

    /**
     * Invoice date in unix timestamp
     */
    @JsonProperty("Date")
    private String date;

    /**
     * name of the seller on the invoice
     */
    @JsonProperty("Seller Company Name")
    private String sellerCompanyName;

    /**
     * total amount of the invoice
     */
    @JsonProperty("Total Amount")
    private String totalAmount;
}
