package pl.futurecollars.invoicing.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

@Slf4j
public class FileBasedDatabase implements Database {

    private final FileService fileService;
    private final JsonService jsonService;

    public FileBasedDatabase(FileService fileService, JsonService jsonService) {
        this.fileService = fileService;
        this.jsonService = jsonService;
    }

    @Override
    public Invoice save(Invoice invoice) {
        if (!checkIfInvoiceExist(invoice.getId())) {
            log.info("Saving new invoice");
            String json = jsonService.toJson(invoice);
            fileService.write(json);
        }
        return invoice;

    }

    @Override
    public Invoice getById(UUID id) {
        Path path = Paths.get(fileService.getPath());
        if (Files.notExists(path) || !checkIfInvoiceExist(id)) {
            log.warn("Could not find invoice");
            throw new NoSuchElementException();
        }
        return getAll().stream().filter(invoice -> invoice.getId().equals(id)).findFirst().get();
    }

    @Override
    public List<Invoice> getAll() {
        List<String> lines = fileService.readLine();
        List<Invoice> invoiceList = lines.stream().map(s -> {
            try {
                return jsonService.toObject(s);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
        return invoiceList;
    }

    @Override
    public Invoice update(Invoice updatedInvoice) {
        if (checkIfInvoiceExist(updatedInvoice.getId())) {
            Invoice invoice = getAll().stream().filter(i -> i.getId().equals(updatedInvoice.getId())).findFirst().get();
            delete(updatedInvoice.getId());
            invoice.setPurchaser(updatedInvoice.getPurchaser());
            invoice.setVendor(updatedInvoice.getVendor());
            invoice.setDate(updatedInvoice.getDate());
            invoice.setEntries(updatedInvoice.getEntries());
            return save(invoice);
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        fileService.deleteLine(id.toString());
    }

    public boolean checkIfInvoiceExist(UUID id) {
        Path path = Paths.get(fileService.getPath());
        if (Files.notExists(path)) {
            return false;
        }
        List<String> listOfInvoices = fileService.readLine();
        List<UUID> uuidList = listOfInvoices.stream().map(i -> {
            try {
                return jsonService.toObject(i);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).map(invoice -> invoice.getId())
            .collect(Collectors.toList());
        return uuidList.contains(id);
    }
}
