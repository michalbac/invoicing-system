package pl.futurecollars.invoicing.service;

import java.util.List;
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

    public Invoice searchById(int id) {
        return database.getById(id);
    }

    public List<Invoice> getAllInvoices() {
        return database.getAll();
    }

    public Invoice updateInvoice(int id, Invoice invoice) {
        return database.update(id, invoice);
    }

    public void deleteInvoice(int id) {
        database.delete(id);
    }

}
