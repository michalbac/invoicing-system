package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.InMemoryDatabase
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import java.time.LocalDate


class InvoiceServiceTestSuite extends Specification {

    def Database db
    private InvoiceService service

    def setup() {
        db = new InMemoryDatabase();
        service = new InvoiceService(db);
    }

    def "add invoice"() {
        when:
        def Invoice savedInvoice = service.save(TestHelpers.invoice1)

        then:
        savedInvoice != null
        savedInvoice.date == TestHelpers.invoice1.getDate()
        savedInvoice.entries == TestHelpers.invoice1.getEntries()
        savedInvoice.purchaser == TestHelpers.invoice1.getPurchaser()
        savedInvoice.vendor == TestHelpers.invoice1.getVendor()

    }

    def "find invoice by id"() {
        when:
        service.save(TestHelpers.invoice1)
        service.save(TestHelpers.invoice2)
        Invoice foundInvoice = service.searchById(TestHelpers.uuid1)

        then:
        foundInvoice != null
        foundInvoice.date == TestHelpers.invoice1.getDate()
        foundInvoice.entries == TestHelpers.invoice1.getEntries()
        foundInvoice.purchaser == TestHelpers.invoice1.getPurchaser()
        foundInvoice.vendor == TestHelpers.invoice1.getVendor()
    }

    def "find not existing invoice"() {
        when:
        service.save(TestHelpers.invoice1)
        service.save(TestHelpers.invoice2)
        service.searchById(UUID.randomUUID())

        then:
        thrown(NoSuchElementException)
    }

    def "find all invoices"() {
        when:
        service.save(TestHelpers.invoice1)
        service.save(TestHelpers.invoice2)
        List<Invoice> foundInvoicesList = service.getAllInvoices()

        then:
        foundInvoicesList.size() == 2
    }

    def "delete invoice"() {
        when:
        service.save(TestHelpers.invoice1)
        service.save(TestHelpers.invoice2)
        service.deleteInvoice(TestHelpers.uuid1)

        then:
        service.getAllInvoices().size() == 1
    }

    def "update invoice"() {
        when:

        service.save(TestHelpers.invoice1)
        TestHelpers.invoice1.getEntries().remove(1)
        TestHelpers.invoice1.setDate(new LocalDate(2021, 8, 10))
        Invoice updatedInvoice = service.updateInvoice(TestHelpers.invoice1)

        then:
        updatedInvoice.id == TestHelpers.uuid1
        updatedInvoice.date.toString() == "2021-08-10"
        updatedInvoice.entries.size() == 1
        updatedInvoice.purchaser == TestHelpers.invoice1.getPurchaser()
        updatedInvoice.vendor == TestHelpers.invoice1.getVendor()
    }

    def cleanupSpec() {
        TestHelpers.invoice1.getEntries().add(TestHelpers.entry2)
    }
}
