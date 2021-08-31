package pl.futurecollars.invoicing.db;

import java.util.List;
import pl.futurecollars.invoicing.model.Invoice;

public interface Database {

    Invoice save(Invoice invoice);

    Invoice getById(int id);

    List<Invoice> getAll();

    Invoice update(int id, Invoice updatedInvoice);

    void delete(int id);
}
