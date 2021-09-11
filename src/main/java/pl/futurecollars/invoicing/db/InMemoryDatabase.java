package pl.futurecollars.invoicing.db;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import pl.futurecollars.invoicing.model.Invoice;

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
        Invoice invoice = invoices.get(updatedInvoice.getId());
        invoice.setDate(updatedInvoice.getDate());
        invoice.setEntries(updatedInvoice.getEntries());
        invoice.setVendor(updatedInvoice.getVendor());
        invoice.setPurchaser(updatedInvoice.getPurchaser());
        return invoice;
    }

    @Override
    public void delete(UUID id) throws NoSuchElementException {
        if (invoices.containsKey(id)) {
            invoices.remove(id);
        } else {
            throw new NoSuchElementException();
        }

    }
}
