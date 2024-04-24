package ec.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    public byte[] getImmagineUrl() {
        return immagineUrl;
    }

    public void setImmagineUrl(byte[] immagineUrl){
        this.immagineUrl=immagineUrl;
    }
    public void setTemp_Url(String temp_url) {
        this.temp_url = temp_url;
    }

    public String getTemp_url(){
        return temp_url;
    }

}
