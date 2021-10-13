package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.exceptions.CompanyNotFoundException;
import pl.futurecollars.invoicing.model.Calculation;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

@Service
@AllArgsConstructor
public class TaxCalculatorService {

    private final Database database;

    public Calculation getCalculation(String taxId) throws CompanyNotFoundException {
        if (!checkIfTaxIdExists(taxId)) {
            throw new CompanyNotFoundException("Company with tax id %s not existing in database" + taxId);
        }
        return Calculation.builder()
            .costs(costs(taxId))
            .income(income(taxId))
            .earnings(earnings(taxId))
            .inputVat(inputVat(taxId))
            .outputVat(outputVat(taxId))
            .payableVat(payableVat(taxId))
            .taxId(taxId)
            .build();
    }

    private BigDecimal costs(String taxId) {
        return database.visit(buyerPredicate(taxId), InvoiceEntry::getPrice);
    }

    private BigDecimal income(String taxId) {
        return database.visit(sellerPredicate(taxId), InvoiceEntry::getPrice);
    }

    private BigDecimal earnings(String taxId) {
        return income(taxId).subtract(costs(taxId));
    }

    private BigDecimal inputVat(String taxId) {
        return database.visit(buyerPredicate(taxId), InvoiceEntry::getVatValue);
    }

    private BigDecimal outputVat(String taxId) {
        return database.visit(sellerPredicate(taxId), InvoiceEntry::getVatValue);
    }

    private BigDecimal payableVat(String taxId) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal vatBalance = outputVat(taxId).subtract(inputVat(taxId));
        if (vatBalance.compareTo(BigDecimal.ZERO) == 1) {
            result = vatBalance;
        }
        return result;
    }

    public Predicate<Invoice> sellerPredicate(String taxId) {
        return i -> i.getVendor().getTaxId().equals(taxId);
    }

    public Predicate<Invoice> buyerPredicate(String taxId) {
        return i -> i.getPurchaser().getTaxId().equals(taxId);
    }

    public boolean checkIfTaxIdExists(String taxId) {
        boolean result = false;
        long vendorCount = database.getAll().stream()
            .filter(i -> i.getVendor().getTaxId().equals(taxId))
            .count();
        long purchaserCount = database.getAll().stream()
            .filter(i -> i.getPurchaser().getTaxId().equals(taxId))
            .count();
        if (vendorCount + purchaserCount > 0) {
            result = true;
        }

        return result;
    }

}
