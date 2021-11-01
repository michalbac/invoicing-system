package pl.futurecollars.invoicing.helpers

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {
    public static Company company1 = new Company(UUID.randomUUID(), "PL5002018899", "00-001 Warszawa, Francuska 1", BigDecimal.valueOf(10), BigDecimal.valueOf(9))
    public static Company company2 = new Company(UUID.randomUUID(), "PL5201111333", "00-001 Warszawa, Prosta 7",  BigDecimal.valueOf(30), BigDecimal.valueOf(18))
    public static InvoiceEntry entry1 = new InvoiceEntry("bread", 2.0, 0.46, Vat.STANDARD, null, false)
    public static InvoiceEntry entry2 = new InvoiceEntry("milk", 3.0, 0.24, Vat.REDUCED, "12345", true)
    public static List<InvoiceEntry> entries1 = new ArrayList<InvoiceEntry>(Arrays.asList(entry1, entry2))
    public static List<InvoiceEntry> entries2 = new ArrayList<InvoiceEntry>(Arrays.asList(entry1))
    public static Invoice invoice1 = new Invoice(LocalDate.now(), company1, company2, entries1)
    public static Invoice invoice2 = new Invoice(LocalDate.now(), company2, company1, entries2)
    public static UUID uuid1 = invoice1.getId()
    public static UUID uuid2 = invoice2.getId()
}
