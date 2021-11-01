package pl.futurecollars.invoicing.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@Service
public class JsonService {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonService() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public Invoice toObject(String json) throws IOException {
        return mapper.readValue(json.getBytes(), Invoice.class);
    }

    public String invoiceToJson(Invoice invoice) {
        try {
            return mapper.writeValueAsString(invoice);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String companyToJson(Company company) {
        try {
            return mapper.writeValueAsString(company);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
