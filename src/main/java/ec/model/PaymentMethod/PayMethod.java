package ec.model.PaymentMethod;

import java.util.Arrays;
import java.util.Objects;

public class PayMethod {
    private String circuito;
    private String  numCarta;

    private String dataScadenza;

    private String titolareCarta;

    private byte[] cvvHash;
    private byte[] saltCvv;
    private int numId;

    public PayMethod() {
        this.circuito = "";
        this.numCarta = "";
        this.dataScadenza = "";
        this.titolareCarta = "";
        this.cvvHash = null;
        this.saltCvv=null;
        this.numId=-1;
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

    public byte[] getCvv() {
        return cvvHash;
    }

    public void setCvv(byte[] cvv) {
        this.cvvHash = cvv;
    }

    public byte[] getSalt() {
        return saltCvv;
    }

    public void setSalt(byte[] salt) {
        this.saltCvv = salt;
    }

    public int getNumId() {
        return numId;
    }

    public void setNumId(int numId) {
        this.numId = numId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayMethod payMethod = (PayMethod) o;
        return numId == payMethod.numId && Objects.equals(circuito, payMethod.circuito) && Objects.equals(numCarta, payMethod.numCarta) && Objects.equals(dataScadenza, payMethod.dataScadenza) && Objects.equals(titolareCarta, payMethod.titolareCarta) && Arrays.equals(cvvHash, payMethod.cvvHash) && Arrays.equals(saltCvv, payMethod.saltCvv);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(circuito, numCarta, dataScadenza, titolareCarta, numId);
        result = 31 * result + Arrays.hashCode(cvvHash);
        result = 31 * result + Arrays.hashCode(saltCvv);
        return result;
    }

    @Override
    public String toString() {
        return "PayMethod{" +
                "circuito='" + circuito + '\'' +
                ", numCarta='" + numCarta + '\'' +
                ", dataScadenza='" + dataScadenza + '\'' +
                ", titolareCarta='" + titolareCarta + '\'' +
                '}';
    }
}
