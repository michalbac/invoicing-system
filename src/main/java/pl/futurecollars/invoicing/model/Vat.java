package pl.futurecollars.invoicing.model;

public enum Vat {

    STANDARD(0.23),
    REDUCED(0.08),
    EXEMPT(0.00);

    private double value;

    Vat(double v) {
        this.value = v;
    }

    public double getValue() {
        return value;
    }
}
