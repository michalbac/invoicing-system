package pl.futurecollars.invoicing.model;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Invoice {

    private final int id;
    private LocalDate date;
    private Company vendor;
    private Company purchaser;
    private List<InvoiceEntry> entries;
}
