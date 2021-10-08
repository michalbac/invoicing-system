package pl.futurecollars.invoicing.config;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.FileBasedDatabase;
import pl.futurecollars.invoicing.db.InMemoryDatabase;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

@Configuration
@Slf4j
public class DatabaseConfiguration {

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    public Database fileBasedDatabase(FileService fileService, JsonService jsonService,
                                      @Value("${invoicing-system.database.file}") String path
    ) throws IOException {
        log.info("Creating file db");
        fileService.setPath(path);
        return new FileBasedDatabase(fileService, jsonService);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
    public Database fileBasedDatabase() throws IOException {
        log.info("Creating in memory db");
        return new InMemoryDatabase();
    }
}
