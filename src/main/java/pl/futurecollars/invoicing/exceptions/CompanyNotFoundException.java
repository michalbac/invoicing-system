package pl.futurecollars.invoicing.exceptions;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(String message) {
        super(message);
    }
}
