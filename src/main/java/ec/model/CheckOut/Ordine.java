package ec.model.CheckOut;

public class Ordine {
    static int idFattura=0;
    String codiceFattura="";
    String utenteId="";
    int codAdress;
    int codMthod;
    Ordine(){
        idFattura++;
        codiceFattura="TVL00" + idFattura;
        int codAdress=-1;
    }


}
