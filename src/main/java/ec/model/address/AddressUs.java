package ec.model.address;

import java.util.Objects;

public class AddressUs {
    private String cap;
    private  String citta;
    private String provincia;
    private String via;
    private int numCiv;
    private String preferenze;

    private int num_ID;
    public AddressUs(){
        cap="";
        citta="";
        provincia="";
        via="";
        numCiv=-1;
        preferenze="";
        num_ID=-1;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public int getNumCiv() {
        return numCiv;
    }

    public void setNumCiv(int numCiv) {
        this.numCiv = numCiv;
    }

    public String getPreferenze() {
        return preferenze;
    }

    public void setPreferenze(String preferenze) {
        this.preferenze = preferenze;
    }
    public void setNum_ID (int num){this.num_ID= this.num_ID+num;}

    public int getNum_ID (){return num_ID;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressUs address = (AddressUs) o;
        return numCiv == address.numCiv && Objects.equals(cap, address.cap) && Objects.equals(citta, address.citta) && Objects.equals(provincia, address.provincia) && Objects.equals(via, address.via) && Objects.equals(preferenze, address.preferenze);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cap, citta, provincia, via, numCiv, preferenze);
    }

    @Override
    public String toString() {
        return via + ", " + numCiv + ", "+
                "\n" + citta + " (" +provincia + ")" + ", " + cap + "\n" +
                "Preferenze specificate= " + preferenze + '\'';
    }
}
