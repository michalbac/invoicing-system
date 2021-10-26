package pl.futurecollars.invoicing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("pl.futurecollars"))
            .paths(PathSelectors.any())
            .build()
            .tags(
                new Tag("invoice-controller", "Controller used to list / add / get / update / delete invoices"),
                new Tag("calculation-controller", "Controller to calculate taxes and income/costs")
            )
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .description("Application to manage set of invoices")
            .license("No license available")
            .title("Invoicing System App")
            .contact(new Contact(
                "Michał Bac",
                "http://google.com",
                "Michal@gmail.com")
            )
            .build();
    }
}
