package pl.futurecollars.invoicing.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.exceptions.CompanyNotFoundException
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Calculation
import pl.futurecollars.invoicing.model.Company
import spock.lang.Specification

import static spock.util.matcher.HamcrestMatchers.closeTo
import static spock.util.matcher.HamcrestSupport.that

@ActiveProfiles("test")
@SpringBootTest
class TaxCalculatorServiceTestSuite extends Specification{

    @Autowired
    TaxCalculatorService service;

    def "should calculate taxes and income/costs for proper id with no payable VAT"() {
        when:
        Calculation calculation = service.getCalculation(TestHelpers.company1)

        then:
        that calculation.getCosts(), closeTo(new BigDecimal(200), 0.1)
        that calculation.getEarnings(), closeTo(new BigDecimal(-35), 0.1)
        that calculation.getIncome(), closeTo(new BigDecimal(165), 0.1)
        that calculation.getInputVat(), closeTo(new BigDecimal(23.5), 0.1)
        that calculation.getOutputVat(), closeTo(new BigDecimal(35.7), 0.1)
        that calculation.getPayableVat(), closeTo(new BigDecimal(12.2), 0.1)
        that calculation.getHealthInsurance9(), closeTo(new BigDecimal(10), 0.1)
        that calculation.getHealthInsurance775(), closeTo(new BigDecimal(8.61), 0.1)
        that calculation.getPensionInsurance(), closeTo(new BigDecimal(9), 0.1)
        that calculation.getTaxBase(), closeTo(new BigDecimal(-44), 0.1)
        that calculation.getIncomeTax(), closeTo(new BigDecimal(-8.36), 0.1)
        that calculation.getCitToBePaid(), closeTo(new BigDecimal(-17), 0.1)


    }

    def "should calculate taxes and income/costs for proper id with payable VAT"() {
        when:
        Calculation calculation = service.getCalculation(TestHelpers.company2)

        then:
        that calculation.getCosts(), closeTo(new BigDecimal(165), 0.1)
        that calculation.getEarnings(), closeTo(new BigDecimal(35), 0.1)
        that calculation.getIncome(), closeTo(new BigDecimal(200), 0.1)
        that calculation.getInputVat(), closeTo(new BigDecimal(35.7), 0.1)
        that calculation.getOutputVat(), closeTo(new BigDecimal(23.5), 0.1)
        calculation.getPayableVat()== BigDecimal.ZERO
        that calculation.getHealthInsurance9(), closeTo(new BigDecimal(30), 0.1)
        that calculation.getHealthInsurance775(), closeTo(new BigDecimal(25.83), 0.1)
        that calculation.getPensionInsurance(), closeTo(new BigDecimal(18), 0.1)
        that calculation.getTaxBase(), closeTo(new BigDecimal(17), 0.1)
        that calculation.getIncomeTax(), closeTo(new BigDecimal(3.23), 0.1)
        that calculation.getCitToBePaid(), closeTo(new BigDecimal(-23), 0.1)
    }

    def "should throw error for not found tax ID"(){
        when:
        Company company = new Company(UUID.randomUUID(), "PL1230023100", "Morelowa 2, 00-002 Warszawa", BigDecimal.valueOf(10), BigDecimal.valueOf(4))
        service.getCalculation(company)

        then:
        thrown(CompanyNotFoundException)
    }
}
