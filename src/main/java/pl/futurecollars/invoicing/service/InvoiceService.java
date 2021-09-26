package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.exceptions.InvoiceNotFoundException;
import pl.futurecollars.invoicing.model.Invoice;

@Service
public class InvoiceService {

    Database database;

    public InvoiceService(Database database) {
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

    public void deleteInvoice(UUID id) throws InvoiceNotFoundException {
        if (database.checkIfInvoiceExist(id)) {
            database.delete(id);
        } else {
            throw new InvoiceNotFoundException("Invoice with id %s does not exists in db" + id);
        }
    }

}
