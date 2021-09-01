package pl.futurecollars.invoicing.model;

import java.util.UUID;
import lombok.Data;

@Data
public class Company {

    private final UUID id;
    private final String taxId;
    private final String address;
}
