package ec.model.CheckOut;

public class Ordine {
    private int numId;
    private String codiceFattura="";
    private String utenteId="";
    private int codAdress;
    private int codMethod;
    public Ordine(){
        codiceFattura="";
        numId=-1;
        codAdress=-1;
        codMethod=-1;
    }

    Ordine (int numID){
        //creazione per ordini che hanno già un codice fattura ma non
        this.codiceFattura= "TAV0" + numID;
        codAdress=-1;
        codMethod=-1;
    }

    public int getNumId() {
        return numId;
    }

    public void setNumId(int numId) {
        this.numId = numId;
        this.codiceFattura= "TAV0" + numId;
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
