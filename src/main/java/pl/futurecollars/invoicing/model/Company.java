package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Company {

    private UUID id;
    private String taxId;
    private String address;

    @JsonCreator
    public Company(@JsonProperty("id") UUID id, @JsonProperty("taxId") String taxId, @JsonProperty("address") String address) {
        this.id = id;
        this.taxId = taxId;
        this.address = address;
    }
}
