package pl.futurecollars.invoicing.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.exceptions.CompanyNotFoundException
import pl.futurecollars.invoicing.model.Calculation
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
        String taxId = "PL5002018899"
        Calculation calculation = service.getCalculation(taxId)

        then:
        that calculation.getCosts(), closeTo(new BigDecimal(200), 0.1)
        that calculation.getEarnings(), closeTo(new BigDecimal(170), 0.1)
        that calculation.getIncome(), closeTo(new BigDecimal(370), 0.1)
        that calculation.getInputVat(), closeTo(new BigDecimal(23.5), 0.1)
        that calculation.getOutputVat(), closeTo(new BigDecimal(82.1), 0.1)
        that calculation.getPayableVat(), closeTo(new BigDecimal(58.6), 0.1)

    }

    def "should calculate taxes and income/costs for proper id with payable VAT"() {
        when:
        String taxId = "PL5201111333"
        Calculation calculation = service.getCalculation(taxId)

        then:
        that calculation.getCosts(), closeTo(new BigDecimal(370), 0.1)
        that calculation.getEarnings(), closeTo(new BigDecimal(-170), 0.1)
        that calculation.getIncome(), closeTo(new BigDecimal(200), 0.1)
        that calculation.getInputVat(), closeTo(new BigDecimal(82.1), 0.1)
        that calculation.getOutputVat(), closeTo(new BigDecimal(23.5), 0.1)
        calculation.getPayableVat()== BigDecimal.ZERO
    }

    def "should throw error for not found tax ID"(){
        when:
        String taxId = "PL5000000000"
        service.getCalculation(taxId)

        then:
        thrown(CompanyNotFoundException)
    }
}
