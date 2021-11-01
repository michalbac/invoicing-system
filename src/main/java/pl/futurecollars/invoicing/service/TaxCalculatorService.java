package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.exceptions.CompanyNotFoundException;
import pl.futurecollars.invoicing.model.Calculation;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

@Service
@AllArgsConstructor
public class TaxCalculatorService {

    private final Database database;

    public Calculation getCalculation(Company company) throws CompanyNotFoundException {
        String taxId = company.getTaxId();
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
            .pensionInsurance(company.getPensionInsurance())
            .healthInsurance9(company.getHealthInsurance())
            .taxBase(taxBase(company))
            .incomeTax(incomeTax(company))
            .healthInsurance775(healthInsurance_7_75(company))
            .citToBePaid(citToBePaid(company))
            .build();
    }

    private BigDecimal costs(String taxId) {
        return database.visit(buyerPredicate(taxId), InvoiceEntry::getPrice).add(calculateNonDeductibleVat(taxId));
    }

    private BigDecimal income(String taxId) {
        return database.visit(sellerPredicate(taxId), InvoiceEntry::getPrice);
    }

    private BigDecimal earnings(String taxId) {
        return income(taxId).subtract(costs(taxId));
    }

    private BigDecimal inputVat(String taxId) {
        return database.visit(buyerPredicate(taxId), InvoiceEntry::getVatValue).subtract(calculateNonDeductibleVat(taxId));
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

    public BigDecimal calculateNonDeductibleVat(String taxId) {
        BigDecimal vatRelatedToCarsUsedForPersonalPurposes = database.getAll().stream()
            .filter(i -> i.getPurchaser().getTaxId().equals(taxId))
            .flatMap(i -> i.getEntries().stream())
            .filter(e -> e.isCarUsedForPersonalPurpose())
            .map(e -> e.getVatValue())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal nonDeductibleVat = vatRelatedToCarsUsedForPersonalPurposes.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        return nonDeductibleVat;
    }

    public BigDecimal taxBase(Company company) {
        return income(company.getTaxId()).subtract(costs(company.getTaxId())).subtract(company.getPensionInsurance())
            .setScale(0, RoundingMode.HALF_UP);
    }

    public BigDecimal incomeTax(Company company) {
        return taxBase(company).multiply(BigDecimal.valueOf(0.19)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal healthInsurance_7_75(Company company) {
        return company.getHealthInsurance().divide(BigDecimal.valueOf(0.09), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(0.0775))
            .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal citToBePaid(Company company) {
        return incomeTax(company).subtract(healthInsurance_7_75(company)).setScale(0, RoundingMode.HALF_UP);
    }

}
