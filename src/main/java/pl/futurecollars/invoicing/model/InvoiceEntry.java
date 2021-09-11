package pl.futurecollars.invoicing.model;

import lombok.Data;

@Data
public class InvoiceEntry {

    private final String description;
    private final double price;
    private final double vatValue;
    private final Vat vatRate;
}
