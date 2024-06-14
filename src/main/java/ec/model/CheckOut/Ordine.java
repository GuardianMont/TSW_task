package ec.model.CheckOut;

public class Ordine {
    private static int idFattura=0;
    private String codiceFattura="";
    private String utenteId="";
    private int codAdress;
    private int codMethod;
    public Ordine(){ //serve pre creare un nuovo codice fattura
        idFattura++;
        codiceFattura="TVL00" + idFattura;
        codAdress=-1;
        codMethod=-1;
    }

    Ordine (boolean alreadycreate){
        //creazione per ordini che hanno già un codice fattura ma non
        codiceFattura="";
        codAdress=-1;
        codMethod=-1;
    }


    public String getCodiceFattura() {
        return codiceFattura;
    }

    public void setCodiceFattura(String codiceFattura){
        this.codiceFattura=codiceFattura;
    }

    public String getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(String utenteId) {
        this.utenteId = utenteId;
    }

    public int getCodAdress() {
        return codAdress;
    }

    public void setCodAdress(int codAdress) {
        this.codAdress = codAdress;
    }

    public int getCodMethod() {
        return codMethod;
    }

    public void setCodMethod(int codMethod) {
        this.codMethod = codMethod;
    }
}
