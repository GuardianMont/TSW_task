package ec.model.user;

import ec.model.address.AddressUs;

import java.util.ArrayList;

public class UserBean {
    private String username;
    private String nome;
    private String cognome;
    private String email; //check
    private String phoneNumber; //check
    private byte[] password; //check
    private byte[] salt;

    private ArrayList<AddressUs> indirizzi;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public  byte[] getPassword() {
        return password;
    }

    public byte[] getSalt(){ return salt; };

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public void setSalt(byte[] salt){ this.salt = salt; }

    public ArrayList<AddressUs> getIndirizzi(){ return indirizzi;}

    public void addIndirizzo (AddressUs Indirizzo){
        if (!indirizzi.contains(Indirizzo)){
            indirizzi.add(Indirizzo);
        }
    }
}
