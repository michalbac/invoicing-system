package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.InMemoryDatabase
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Specification

import java.time.LocalDate

class InvoiceServiceTestSuite extends Specification {

    private Database db
    private InvoiceService service
    private List<Invoice> invoices
    UUID uuid1 = UUID.randomUUID()
    UUID uuid2 = UUID.randomUUID()

    def setup() {
        db = new InMemoryDatabase();
        service = new InvoiceService(db);
        Company company1 = new Company(uuid1, "PL5201111333", "00-001 Warszawa, Długa 1")
        Company company2 = new Company(uuid2, "PL5201111333", "00-001 Warszawa, Prosta 7")
        InvoiceEntry entry1 = new InvoiceEntry("bread", 2.0, 0.46, Vat.STANDARD)
        InvoiceEntry entry2 = new InvoiceEntry("milk", 3.0, 0.24, Vat.REDUCED)
        List<InvoiceEntry> entries1 = new ArrayList<InvoiceEntry>();
        entries1.add(entry1)
        List<InvoiceEntry> entries2 = new ArrayList<InvoiceEntry>();
        entries1.add(entry2)
        Invoice invoice1 = new Invoice(uuid1, LocalDate.now(), company1, company2, entries1)
        Invoice invoice2 = new Invoice(uuid2, LocalDate.now(), company2, company1, entries2)
        invoices = new ArrayList<>()
        invoices.add(invoice1)
        invoices.add(invoice2)
    }

    def "add invoice"() {
        when:
        def Invoice savedInvoice = service.save(invoices.get(0))

        then:
        savedInvoice != null
        savedInvoice.date == invoices.get(0).date
        savedInvoice.entries == invoices.get(0).entries
        savedInvoice.purchaser == invoices.get(0).purchaser
        savedInvoice.vendor == invoices.get(0).vendor
    }

    def "find invoice by id"() {
        when:
        service.save(invoices.get(0))
        service.save(invoices.get(1))
        Invoice foundInvoice = service.searchById(uuid1)

        then:
        foundInvoice != null
        foundInvoice.date == invoices.get(0).date
        foundInvoice.entries == invoices.get(0).entries
        foundInvoice.purchaser == invoices.get(0).purchaser
        foundInvoice.vendor == invoices.get(0).vendor
    }

    def "find not existing invoice"() {
        when:
        service.save(invoices.get(0))
        service.save(invoices.get(1))
        service.searchById(UUID.randomUUID())

        then:
        thrown(NoSuchElementException)
    }

    def "find all invoices"() {
        when:
        service.save(invoices.get(0))
        service.save(invoices.get(1))
        List<Invoice> foundInvoicesList = service.getAllInvoices()

        then:
        foundInvoicesList.size() == 2
    }

    def "delete invoice"() {
        when:
        service.save(invoices.get(0))
        service.save(invoices.get(1))
        service.deleteInvoice(uuid1)

        then:
        service.getAllInvoices().size() == 1
    }

    def "update invoice"() {
        when:
        service.save(invoices.get(0))
        invoices.get(0).getEntries().remove(1)
        Invoice updatedInvoice = service.updateInvoice(invoices.get(0))

        then:
        updatedInvoice.id == uuid1
        updatedInvoice.date == invoices.get(0).date
        updatedInvoice.entries.size() == 1
        updatedInvoice.purchaser == invoices.get(0).purchaser
        updatedInvoice.vendor == invoices.get(0).vendor
    }
}
