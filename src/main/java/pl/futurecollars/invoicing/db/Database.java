package pl.futurecollars.invoicing.db;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

public interface Database {

    Invoice save(Invoice invoice);

    Invoice getById(UUID id);

    List<Invoice> getAll();

    Invoice update(Invoice updatedInvoice);

    void delete(UUID id);

    public boolean checkIfInvoiceExist(UUID id);

    default BigDecimal visit(Predicate<Invoice> predicate, Function<InvoiceEntry, BigDecimal> amount) {
        BigDecimal result = BigDecimal.ZERO;
        List<Invoice> filteredInvoices = getAll().stream().filter(predicate).collect(Collectors.toList());
        for (Invoice invoice : filteredInvoices) {
            for (InvoiceEntry entry : invoice.getEntries()) {
                result = result.add(amount.apply(entry));
            }
        }
        return result;
    }
}
