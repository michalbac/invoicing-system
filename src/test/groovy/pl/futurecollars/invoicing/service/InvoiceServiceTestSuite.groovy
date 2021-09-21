package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.InMemoryDatabase
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate

class InvoiceServiceTestSuite extends Specification {

    def Database db
    private InvoiceService service
    @Shared
    Company company1 = new Company(UUID.randomUUID(), "PL5201111333", "00-001 Warszawa, Francuska 1")
    @Shared
    Company company2 = new Company(UUID.randomUUID(), "PL5201111333", "00-001 Warszawa, Prosta 7")
    @Shared
    InvoiceEntry entry1 = new InvoiceEntry("bread", 2.0, 0.46, Vat.STANDARD)
    @Shared
    InvoiceEntry entry2 = new InvoiceEntry("milk", 3.0, 0.24, Vat.REDUCED)
    @Shared
    List<InvoiceEntry> entries1 = new ArrayList<InvoiceEntry>(Arrays.asList(entry1, entry2))
    @Shared
    List<InvoiceEntry> entries2 = new ArrayList<InvoiceEntry>(Arrays.asList(entry1))
    @Shared
    Invoice invoice1 = new Invoice(LocalDate.now(), company1, company2, entries1)
    @Shared
    Invoice invoice2 = new Invoice(LocalDate.now(), company2, company1, entries2)
    @Shared
    UUID uuid1 = invoice1.getId()
    @Shared
    UUID uuid2 = invoice2.getId()

    def setup(){
        db = new InMemoryDatabase();
        service = new InvoiceService(db);
    }

    def "add invoice"() {
        when:
        def Invoice savedInvoice = service.save(invoice1)

        then:
        savedInvoice != null
        savedInvoice.date == invoice1.getDate()
        savedInvoice.entries == invoice1.getEntries()
        savedInvoice.purchaser == invoice1.getPurchaser()
        savedInvoice.vendor == invoice1.getVendor()
    }

    def "find invoice by id"() {
        when:
        service.save(invoice1)
        service.save(invoice2)
        Invoice foundInvoice = service.searchById(uuid1)

        then:
        foundInvoice != null
        foundInvoice.date == invoice1.getDate()
        foundInvoice.entries == invoice1.getEntries()
        foundInvoice.purchaser == invoice1.getPurchaser()
        foundInvoice.vendor == invoice1.getVendor()
    }

    def "find not existing invoice"() {
        when:
        service.save(invoice1)
        service.save(invoice2)
        service.searchById(UUID.randomUUID())

        then:
        thrown(NoSuchElementException)
    }

    def "find all invoices"() {
        when:
        service.save(invoice1)
        service.save(invoice2)
        List<Invoice> foundInvoicesList = service.getAllInvoices()

        then:
        foundInvoicesList.size() == 2
    }

    def "delete invoice"() {
        when:
        service.save(invoice1)
        service.save(invoice2)
        service.deleteInvoice(uuid1)

        then:
        service.getAllInvoices().size() == 1
    }

    def "update invoice"() {
        when:
        service.save(invoice1)
        invoice1.getEntries().remove(1)
        Invoice updatedInvoice = service.updateInvoice(invoice1)

        then:
        updatedInvoice.id == uuid1
        updatedInvoice.date == invoice1.getDate()
        updatedInvoice.entries.size() == 1
        updatedInvoice.purchaser == invoice1.getPurchaser()
        updatedInvoice.vendor == invoice1.getVendor()
    }
}
