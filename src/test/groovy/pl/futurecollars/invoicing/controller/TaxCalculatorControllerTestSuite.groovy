package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Specification

import static org.hamcrest.Matchers.closeTo
import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class TaxCalculatorControllerTestSuite extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService;

    def "get calculation for correct tax id"() {
        when:
        String taxId = "PL5002018899"

        then:
        mockMvc.perform(get("/api/calculation/" + taxId))
                .andExpect(jsonPath('$.costs', is(200D)))
                .andExpect(jsonPath('$.earnings', is(170D)))
                .andExpect(jsonPath('$.income', is(370D)))
                .andExpect(jsonPath('$.inputVat', is(23.5D)))
                .andExpect(jsonPath('$.outputVat', is(82.1D)))
                .andExpect(jsonPath('$.payableVat', is(58.6D)))
                .andExpect(status().isOk())
    }

    def "get calculation for not existing tax id"() {
        when:
        String taxId = "PL5000000000"

        then:
        mockMvc.perform(get("/api/calculation/" + taxId))
                .andExpect(status().isBadRequest())
    }

}
