package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceEntry {

    @ApiModelProperty(value = "Product/service description", required = true, example = "Advisory services")
    private String description;
    @ApiModelProperty(value = "Product/service net price", required = true, example = "100")
    private BigDecimal price;
    @ApiModelProperty(value = "Product/service VAT amount", required = true, example = "23")
    private BigDecimal vatValue;
    @ApiModelProperty(value = "Tax rate", required = true)
    private Vat vatRate;
    @ApiModelProperty(value = "License plate number if entry is related to car")
    private String licensePlateNumber;
    @ApiModelProperty(value = "Indicator if  car is used for personal purposes also")
    private boolean isCarUsedForPersonalPurpose;

    @JsonCreator
    public InvoiceEntry(@JsonProperty("description") String description, @JsonProperty("price") BigDecimal price,
                        @JsonProperty("vatValue") BigDecimal vatValue, @JsonProperty("vatRate") Vat vatRate,
                        @JsonProperty("licensePlateNumber") String licensePlateNumber,
                        @JsonProperty("isCarUsedForPersonalPurpose") boolean isCarUsedForPersonalPurpose) {
        this.description = description;
        this.price = price;
        this.vatValue = vatValue;
        this.vatRate = vatRate;
        this.licensePlateNumber = licensePlateNumber;
        this.isCarUsedForPersonalPurpose = isCarUsedForPersonalPurpose;
    }
}
