package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Specification

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("dev")
class InvoiceControllerTest extends Specification {
    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService;

    def "get empty list of invoices"() {
        when:
        def response = mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        response == "[]"
    }

    def "add one invoice"() {
        String contentAsJson = jsonService.toJson(TestHelpers.invoice1);
        when:
        def response = mockMvc.perform(post("/api").content(contentAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .response
                .contentAsString

        then:
        jsonService.toObject(response).getId() == TestHelpers.invoice1.getId()
    }

    def "update existing invoice"() {
        when:
        String contentAsJson = jsonService.toJson(TestHelpers.invoice1);
        mockMvc.perform(post("/api").content(contentAsJson).contentType(MediaType.APPLICATION_JSON))
        Invoice updatedInvoice = TestHelpers.invoice1
        updatedInvoice.setDate(new LocalDate(2021, 9, 15))
        String updatedContentAsJson = jsonService.toJson(updatedInvoice)

        then:
        mockMvc.perform(put("/api/").content(updatedContentAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.date', is("2021-09-15")))
                .andExpect(status().isAccepted())
    }

    def "delete not existing invoice"() {
        expect:
        mockMvc.perform(delete("/api/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
    }

    def "delete one invoice"() {
        when:
        String contentAsJson1 = jsonService.toJson(TestHelpers.invoice1);
        String contentAsJson2 = jsonService.toJson(TestHelpers.invoice2);
        mockMvc.perform(post("/api").content(contentAsJson1).contentType(MediaType.APPLICATION_JSON))
        mockMvc.perform(post("/api").content(contentAsJson2).contentType(MediaType.APPLICATION_JSON))

        then:
        mockMvc.perform(delete("/api/" + TestHelpers.uuid1))
                .andExpect(status().isAccepted())

        mockMvc.perform(get("/api").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(1)))
    }

    def "get existing invoice"() {
        when:
        String contentAsJson = jsonService.toJson(TestHelpers.invoice1);
        mockMvc.perform(post("/api").content(contentAsJson).contentType(MediaType.APPLICATION_JSON))

        then:
        mockMvc.perform(get("/api/" + TestHelpers.invoice1.getId()))
                .andExpect(jsonPath('$.date', is(TestHelpers.invoice1.getDate().toString())))
                .andExpect(jsonPath('$.id', is(TestHelpers.invoice1.getId().toString())))
                .andExpect(status().isOk())
    }

    def "get not exisiting invoice"() {
        expect:
        mockMvc.perform(get("/api/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
    }
}
