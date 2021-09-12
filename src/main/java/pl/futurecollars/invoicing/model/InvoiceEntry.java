package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceEntry {

    private String description;
    private double price;
    private double vatValue;
    private Vat vatRate;

    @JsonCreator
    public InvoiceEntry(@JsonProperty("description") String description, @JsonProperty("price") double price,
                        @JsonProperty("vatValue") double vatValue, @JsonProperty("vatRate") Vat vatRate) {
        this.description = description;
        this.price = price;
        this.vatValue = vatValue;
        this.vatRate = vatRate;
    }
}
