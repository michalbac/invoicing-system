package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Calculation {

    @JsonProperty
    private String taxId;
    @JsonProperty
    private BigDecimal inputVat;
    @JsonProperty
    private BigDecimal outputVat;
    @JsonProperty
    private BigDecimal income;
    @JsonProperty
    private BigDecimal costs;
    @JsonProperty
    private BigDecimal earnings;
    @JsonProperty
    private BigDecimal payableVat;
    @JsonProperty
    private BigDecimal pensionInsurance;
    @JsonProperty
    private BigDecimal taxBase;
    @JsonProperty
    private BigDecimal incomeTax;
    @JsonProperty
    private BigDecimal healthInsurance9;
    @JsonProperty
    private BigDecimal healthInsurance775;
    @JsonProperty
    private BigDecimal citToBePaid;
}
