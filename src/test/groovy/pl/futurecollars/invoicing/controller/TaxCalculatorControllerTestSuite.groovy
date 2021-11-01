package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Specification


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class TaxCalculatorControllerTestSuite extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    def "get calculation for correct tax id"() {
        when:
        String contentAsJson = jsonService.companyToJson(TestHelpers.company2)

        then:
        mockMvc.perform(post("/api/calculation").content(contentAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.costs', is(165D)))
                .andExpect(jsonPath('$.earnings', is(35D)))
                .andExpect(jsonPath('$.income', is(200D)))
                .andExpect(jsonPath('$.inputVat', is(35.7D)))
                .andExpect(jsonPath('$.outputVat', is(23.5D)))
                .andExpect(jsonPath('$.payableVat', is(0)))
                .andExpect(jsonPath('$.pensionInsurance', is(18)))
                .andExpect(jsonPath('$.taxBase', is(17)))
                .andExpect(jsonPath('$.incomeTax', is(3.23D)))
                .andExpect(jsonPath('$.healthInsurance9', is(30)))
                .andExpect(jsonPath('$.healthInsurance775', is(25.83D)))
                .andExpect(jsonPath('$.citToBePaid', is(-23)))
                .andExpect(status().isOk())
    }

    def "get calculation for not existing tax id"() {
        when:
        Company company = new Company(UUID.randomUUID(), "PL5000000000", "Test address", BigDecimal.ZERO, BigDecimal.ZERO)
        String contentAsJson = jsonService.companyToJson(company)

        then:
        mockMvc.perform(post("/api/calculation").content(contentAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
    }

}
