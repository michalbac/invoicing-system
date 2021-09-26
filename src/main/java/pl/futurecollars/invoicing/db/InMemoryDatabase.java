package pl.futurecollars.invoicing.db;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.model.Invoice;

@Repository
@Primary
public class InMemoryDatabase implements Database {

    private Map<UUID, Invoice> invoices = new HashMap<>();

    @Override
    public Invoice save(Invoice invoice) {
        invoices.put(invoice.getId(), invoice);
        return invoice;
    }

    @Override
    public Invoice getById(UUID id) throws NoSuchElementException {
        if (invoices.containsKey(id)) {
            return invoices.get(id);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<Invoice> getAll() {
        return invoices.values().stream()
            .collect(Collectors.toList());
    }

    @Override
    public Invoice update(Invoice updatedInvoice) {
        return invoices.put(updatedInvoice.getId(), updatedInvoice);
    }

    @Override
    public void delete(UUID id) {
        invoices.remove(id);
    }

    @Override
    public boolean checkIfInvoiceExist(UUID id) {
        if (invoices.containsKey(id)) {
            return true;
        }
        return false;
    }
}
