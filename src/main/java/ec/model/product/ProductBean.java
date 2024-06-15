package ec.model.product;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class ProductBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String descrizione;
    private double prezzo;
    private double fasciaIva;
    private String dimensioni;
    private int disponibilita;
    private String categoria;
    private String colore;
    private byte[] immagineUrl; // Cambiato da byte[] a String per test
    //per ora riporto a byte
    //semplifica la comunicazione c
    private String temp_url;
    // Costruttore vuoto
    public ProductBean() {
        id=0;
        nome="";
        descrizione="";
        prezzo=0;
        fasciaIva=0;
        dimensioni="";
        disponibilita=0;
        categoria="";
        colore="";
        immagineUrl=null;
        temp_url=null;
    }

    // Metodi getter e setter per tutti gli attributi
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public double getFasciaIva() {
        return fasciaIva;
    }

    public void setFasciaIva(double fasciaIva) {
        this.fasciaIva = fasciaIva;
    }

    public String getDimensioni() {
        return dimensioni;
    }

    public void setDimensioni(String dimensioni) {
        this.dimensioni = dimensioni;
    }

    public int getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(int disponibilita) {
        this.disponibilita = disponibilita;
    }

    public void updateDisponibilita (int quantitaAcquistata){
        this.disponibilita = this.disponibilita - quantitaAcquistata;
        if (this.disponibilita<0) this.disponibilita=0;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }
    //rappresentazione in Byte dell'immagine.
    //necessaria per class 64 se no non si vede niente
    public byte[] getImmagineUrl() {
        return immagineUrl;
    }

    public void setImmagineUrl(byte[] immagineUrl){
        this.immagineUrl=immagineUrl;
    }

    //temp_url fa riferimento al path dell'immagine all'interno del server
    public void setTemp_Url(String temp_url) {
        this.temp_url = temp_url;
    }

    public String getTemp_url(){
        return temp_url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductBean that = (ProductBean) o;
        return id == that.id && Double.compare(prezzo, that.prezzo) == 0 && Double.compare(fasciaIva, that.fasciaIva) == 0 && disponibilita == that.disponibilita && Objects.equals(nome, that.nome) && Objects.equals(descrizione, that.descrizione) && Objects.equals(dimensioni, that.dimensioni) && Objects.equals(categoria, that.categoria) && Objects.equals(colore, that.colore) && Arrays.equals(immagineUrl, that.immagineUrl) && Objects.equals(temp_url, that.temp_url);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, nome, descrizione, prezzo, fasciaIva, dimensioni, disponibilita, categoria, colore, temp_url);
        result = 31 * result + Arrays.hashCode(immagineUrl);
        return result;
    }
}
