package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class Invoice {

    private UUID id;
    private LocalDate date;
    private Company vendor;
    private Company purchaser;
    private List<InvoiceEntry> entries;

    public Invoice(@JsonProperty("date") LocalDate date, @JsonProperty("vendor") Company vendor,
                   @JsonProperty("purchaser") Company purchaser,
                   @JsonProperty("entries") List<InvoiceEntry> entries) {
        this.id = UUID.randomUUID();
        this.date = date;
        this.vendor = vendor;
        this.purchaser = purchaser;
        this.entries = entries;
    }
}
