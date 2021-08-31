package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Specification

import java.time.LocalDate

class InMemoryDbTestSuite extends Specification {

    private Database db
    private List<Invoice> invoices

    def setup() {
        db = new InMemoryDatabase();
        Company company1 = new Company(1, "PL5201111333", "00-001 Warszawa, Długa 1")
        Company company2 = new Company(2, "PL5201111333", "00-001 Warszawa, Prosta 7")
        InvoiceEntry entry1 = new InvoiceEntry("bread", 2.0, 0.46, Vat.STANDARD)
        InvoiceEntry entry2 = new InvoiceEntry("milk", 3.0, 0.24, Vat.REDUCED)
        List<InvoiceEntry> entries1 = new ArrayList<InvoiceEntry>();
        entries1.add(entry1)
        List<InvoiceEntry> entries2 = new ArrayList<InvoiceEntry>();
        entries1.add(entry2)
        Invoice invoice1 = new Invoice(1, LocalDate.now(), company1, company2, entries1)
        Invoice invoice2 = new Invoice(2, LocalDate.now(), company2, company1, entries2)
        invoices = new ArrayList<>()
        invoices.add(invoice1)
        invoices.add(invoice2)
    }

    def "add invoice to the db"() {
        when:
        def Invoice savedInvoice = db.save(invoices.get(0))

        then:
        savedInvoice != null
        savedInvoice.date == invoices.get(0).date
        savedInvoice.entries == invoices.get(0).entries
        savedInvoice.purchaser == invoices.get(0).purchaser
        savedInvoice.vendor == invoices.get(0).vendor
    }

    def "find by id in db"() {
        when:
        db.save(invoices.get(0))
        def Invoice foundInvoice = db.getById(1);

        then:
        foundInvoice != null
        foundInvoice.date == invoices.get(0).date
        foundInvoice.entries == invoices.get(0).entries
        foundInvoice.purchaser == invoices.get(0).purchaser
        foundInvoice.vendor == invoices.get(0).vendor
    }

    def "find not existing invoice in db"() {
        when:
        db.save(invoices.get(0))
        db.save(invoices.get(1))
        db.delete(3)

        then:
        thrown(NoSuchElementException)

    }

    def "get all invoices from db"() {
        when:
        db.save(invoices.get(0))
        db.save(invoices.get(1))
        def List<Invoice> invoicesFromDb = db.getAll()

        then:
        invoicesFromDb.size() == 2
    }

    def "delete invoice from db"() {
        when:
        db.save(invoices.get(0))
        db.save(invoices.get(1))
        db.delete(1);

        then:
        db.getAll().size() == 1
    }

    def "update invoice in db"() {
        when:
        db.save(invoices.get(0))
        Invoice updatedInvoice = db.update(1, invoices.get(1))

        then:
        updatedInvoice.id == 1
        updatedInvoice.date == invoices.get(1).date
        updatedInvoice.entries == invoices.get(1).entries
        updatedInvoice.purchaser == invoices.get(1).purchaser
        updatedInvoice.vendor == invoices.get(1).vendor
    }
}