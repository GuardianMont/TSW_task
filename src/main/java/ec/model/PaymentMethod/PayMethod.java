package ec.model.PaymentMethod;

import java.util.Objects;

public class PayMethod {
    private String circuito;
    private String  numCarta;

    private String dataScadenza;

    private String titolareCarta;

    private String cvv;

    public PayMethod() {
        this.circuito = "";
        this.numCarta = "";
        this.dataScadenza = "";
        this.titolareCarta = "";
        this.cvv = "";
    }

    public String getCircuito() {
        return circuito;
    }

    public void setCircuito(String circuito) {
        this.circuito = circuito;
    }

    public String getNumCarta() {
        return numCarta;
    }

    public void setNumCarta(String numCarta) {
        this.numCarta = numCarta;
    }

    public String getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(String dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getTitolareCarta() {
        return titolareCarta;
    }

    public void setTitolareCarta(String titolareCarta) {
        this.titolareCarta = titolareCarta;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayMethod payMethod = (PayMethod) o;
        return Objects.equals(circuito, payMethod.circuito) && Objects.equals(numCarta, payMethod.numCarta) && Objects.equals(dataScadenza, payMethod.dataScadenza) && Objects.equals(titolareCarta, payMethod.titolareCarta) && Objects.equals(cvv, payMethod.cvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(circuito, numCarta, dataScadenza, titolareCarta, cvv);
    }
}
