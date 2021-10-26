package pl.futurecollars.invoicing.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.utils.FileService
import pl.futurecollars.invoicing.utils.JsonService

@ActiveProfiles("prod")
@SpringBootTest
class FileBasedDbTestSuite extends DatabaseTestSuite {

    @Autowired
    FileService fileService
    @Autowired
    JsonService jsonService

    def setup() {
        db = new FileBasedDatabase(fileService, jsonService);
    }
}
