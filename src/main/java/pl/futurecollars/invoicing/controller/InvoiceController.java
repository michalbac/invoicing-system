package pl.futurecollars.invoicing.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.futurecollars.invoicing.exceptions.InvoiceNotFoundException;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@RestController
@RequiredArgsConstructor
public class InvoiceController implements InvoiceApi {

    private final InvoiceService invoiceService;

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @Override
    public Invoice addInvoice(@RequestBody Invoice invoice) {
        try {
            return invoiceService.save(invoice);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide correct invoice", e);
        }
    }

    @Override
    public Invoice updateInvoice(@RequestBody Invoice invoice) {
        try {
            invoiceService.updateInvoice(invoice);
            return invoiceService.searchById(invoice.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide correct invoice", e);
        }
    }

    @Override
    public void deleteInvoice(@PathVariable UUID id) {
        try {
            invoiceService.deleteInvoice(id);
        } catch (InvoiceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provide correct invoice id", e);
        }
    }

    @Override
    public Invoice getInvoice(@PathVariable UUID id) {
        try {
            return invoiceService.searchById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provide correct invoice id", e);
        }
    }
}
