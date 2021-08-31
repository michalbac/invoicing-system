package pl.futurecollars.invoicing.db;

import java.util.ArrayList;
import java.util.List;
import pl.futurecollars.invoicing.model.Invoice;

public class InMemoryDatabase implements Database {

    private List<Invoice> invoices = new ArrayList<>();

    @Override
    public Invoice save(Invoice invoice) {
        invoices.add(invoice);
        return invoice;
    }

    @Override
    public Invoice getById(int id) {
        return invoices.stream()
            .filter(i -> i.getId() == id)
            .findFirst().get();
    }

    @Override
    public List<Invoice> getAll() {
        return invoices;
    }

    @Override
    public Invoice update(int id, Invoice updatedInvoice) {
        Invoice invoice = invoices.stream()
            .filter(i -> i.getId() == id)
            .findFirst().get();
        invoice.setDate(updatedInvoice.getDate());
        invoice.setEntries(updatedInvoice.getEntries());
        invoice.setVendor(updatedInvoice.getVendor());
        invoice.setPurchaser(updatedInvoice.getPurchaser());
        return invoice;
    }

    @Override
    public void delete(int id) {
        Invoice invoice = invoices.stream()
            .filter(i -> i.getId() == id)
            .findFirst().get();
        invoices.remove(invoice);
    }
}
