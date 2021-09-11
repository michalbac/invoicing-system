package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.UUID;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

public class InvoiceService {

    Database database;

    InvoiceService(Database database) {
        this.database = database;
    }

    public Invoice save(Invoice invoice) {
        return database.save(invoice);
    }

    public Invoice searchById(UUID id) {
        return database.getById(id);
    }

    public List<Invoice> getAllInvoices() {
        return database.getAll();
    }

    public Invoice updateInvoice(Invoice invoice) {
        return database.update(invoice);
    }

    public void deleteInvoice(UUID id) {
        database.delete(id);
    }

}
