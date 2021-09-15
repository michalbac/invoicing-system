package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate

abstract class DatabaseTestSuite extends Specification {

    @Shared
    def Database db
    @Shared
    UUID uuid1 = UUID.randomUUID()
    @Shared
    UUID uuid2 = UUID.randomUUID()
    Company company1 = new Company(uuid1, "PL5201111333", "00-001 Warszawa, Francuska 1")
    Company company2 = new Company(uuid2, "PL5201111333", "00-001 Warszawa, Prosta 7")
    InvoiceEntry entry1 = new InvoiceEntry("bread", 2.0, 0.46, Vat.STANDARD)
    InvoiceEntry entry2 = new InvoiceEntry("milk", 3.0, 0.24, Vat.REDUCED)
    List<InvoiceEntry> entries1 = new ArrayList<InvoiceEntry>(Arrays.asList(entry1,entry2))
    List<InvoiceEntry> entries2 = new ArrayList<InvoiceEntry>(Arrays.asList(entry1))
    Invoice invoice1 = new Invoice(uuid1, LocalDate.now(), company1, company2,entries1)
    Invoice invoice2 = new Invoice(uuid2, LocalDate.now(), company2, company1, entries2)


    def "add invoice to the db"() {
        when:
        Invoice savedInvoice = db.save(invoice1)

        then:
        savedInvoice != null
        savedInvoice.date == invoice1.date
        savedInvoice.entries == invoice1.entries
        savedInvoice.purchaser == invoice1.purchaser
        savedInvoice.vendor == invoice1.vendor
    }

    def "find by id in db"() {
        when:
        db.save(invoice1)
        Invoice foundInvoice = db.getById(uuid1)

        then:
        foundInvoice != null
        foundInvoice.date == invoice1.date
        foundInvoice.entries == invoice1.entries
        foundInvoice.purchaser == invoice1.purchaser
        foundInvoice.vendor == invoice1.vendor
    }

    def "find not existing invoice in db"() {
        when:
        db.save(invoice1)
        db.save(invoice2)
        db.getById(UUID.randomUUID())

        then:
        thrown(NoSuchElementException)

    }

    def "get all invoices from db"() {
        when:
        db.save(invoice1)
        db.save(invoice2)

        then:
        db.getAll().contains(invoice1);
        db.getAll().contains(invoice2)
    }

    def "delete invoice from db"() {
        when:
        db.save(invoice1)
        db.save(invoice2)
        db.delete(uuid1)

        then:
        db.getAll().contains(invoice1) == false
    }

    def "update invoice in db"() {
        when:
        db.save(invoice1)
        invoice1.getEntries().remove(1)
        Invoice updatedInvoice = db.update(invoice1)

        then:
        updatedInvoice.id == (uuid1)
        updatedInvoice.date == invoice1.date
        updatedInvoice.entries.size() == 1
        updatedInvoice.purchaser == invoice1.purchaser
        updatedInvoice.vendor == invoice1.vendor
    }

    def cleanupSpec() {
        db.delete(uuid1)
        db.delete(uuid2)
    }


}