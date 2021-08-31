package pl.futurecollars.invoicing.model;

import lombok.Data;

@Data
public class Company {

    private final int id;
    private final String taxId;
    private final String address;
}
