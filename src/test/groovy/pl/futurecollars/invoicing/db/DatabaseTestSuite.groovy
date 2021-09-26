package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Shared
import spock.lang.Specification


abstract class DatabaseTestSuite extends Specification {

    @Shared
    def Database db


    def "add invoice to the db"() {
        when:
        Invoice savedInvoice = db.save(TestHelpers.invoice1)

        then:
        savedInvoice != null
        savedInvoice.date == TestHelpers.invoice1.date
        savedInvoice.entries == TestHelpers.invoice1.entries
        savedInvoice.purchaser == TestHelpers.invoice1.purchaser
        savedInvoice.vendor == TestHelpers.invoice1.vendor
    }

    def "find by id in db"() {
        when:
        db.save(TestHelpers.invoice1)
        Invoice foundInvoice = db.getById(TestHelpers.uuid1)

        then:
        foundInvoice != null
        foundInvoice.date == TestHelpers.invoice1.date
        foundInvoice.entries == TestHelpers.invoice1.entries
        foundInvoice.purchaser == TestHelpers.invoice1.purchaser
        foundInvoice.vendor == TestHelpers.invoice1.vendor
    }

    def "find not existing invoice in db"() {
        when:
        db.save(TestHelpers.invoice1)
        db.save(TestHelpers.invoice2)
        db.getById(UUID.randomUUID())

        then:
        thrown(NoSuchElementException)

    }

    def "get all invoices from db"() {
        when:
        db.save(TestHelpers.invoice1)
        db.save(TestHelpers.invoice2)

        then:
        db.getAll().contains(TestHelpers.invoice1);
        db.getAll().contains(TestHelpers.invoice2)
    }

    def "delete invoice from db"() {
        when:
        db.save(TestHelpers.invoice1)
        db.save(TestHelpers.invoice2)
        db.delete(TestHelpers.uuid1)

        then:
        db.getAll().contains(TestHelpers.invoice1) == false
    }

    def "update invoice in db"() {
        when:
        db.save(TestHelpers.invoice1)
        TestHelpers.invoice1.getEntries().remove(1)
        Invoice updatedInvoice = db.update(TestHelpers.invoice1)

        then:
        updatedInvoice.id == (TestHelpers.uuid1)
        updatedInvoice.date == TestHelpers.invoice1.date
        updatedInvoice.entries.size() == 1
        updatedInvoice.purchaser == TestHelpers.invoice1.purchaser
        updatedInvoice.vendor == TestHelpers.invoice1.vendor
    }

    def cleanupSpec() {
        TestHelpers.invoice1.getEntries().add(TestHelpers.entry2)
        db.delete(TestHelpers.uuid1)
        db.delete(TestHelpers.uuid2)
    }

}