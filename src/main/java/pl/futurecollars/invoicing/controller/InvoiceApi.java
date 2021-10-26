package pl.futurecollars.invoicing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.futurecollars.invoicing.model.Invoice;

@RequestMapping(path = "/api")
@Api(tags = {"invoice-controller"})
public interface InvoiceApi {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "List all invoices from system")
    List<Invoice> getAllInvoices();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add new invoice to system")
    Invoice addInvoice(@RequestBody Invoice invoice);

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Update invoice data with given id")
    Invoice updateInvoice(@RequestBody Invoice invoice);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Delete invoice with given id")
    void deleteInvoice(@PathVariable UUID id);

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get invoice with given id")
    Invoice getInvoice(@PathVariable UUID id);
}
