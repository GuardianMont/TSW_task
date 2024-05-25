DROP DATABASE IF EXISTS tavolando;
CREATE DATABASE tavolando;
USE tavolando;

CREATE TABLE Ordine(
                       codice_fattura VARCHAR(255) PRIMARY KEY,
                       utente_id VARCHAR(255) NOT NULL,
                       data DATE NOT NULL,
                       costo_totale DOUBLE NOT NULL,
                       informazioni TEXT NOT NULL
);
CREATE TABLE Utente(
                       username VARCHAR(255) PRIMARY KEY,
                       nome VARCHAR(255) NOT NULL,
                       cognome VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       n_telefono CHAR(10) NOT NULL,
                       password_hash VARBINARY(255) NOT NULL,
                       salt VARBINARY(16) NOT NULL
);
CREATE TABLE Prodotto(
                         id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         descrizione VARCHAR(255) NOT NULL,
                         prezzo DOUBLE NOT NULL,
                         fascia_iva DOUBLE NOT NULL,
                         dimensioni CHAR(10) NOT NULL,
                         disponibilita SMALLINT NOT NULL,
                         categoria VARCHAR(255) NOT NULL,
                         colore VARCHAR(255) NOT NULL,
                         immagine MEDIUMBLOB
);
CREATE TABLE Indirizzo(
                          utente_id VARCHAR(255) NOT NULL,
                          num SMALLINT NOT NULL,
                          cap CHAR(5) NOT NULL,
                          citta VARCHAR(255) NOT NULL,
                          provincia VARCHAR(255) NOT NULL,
                          via VARCHAR(255) NOT NULL,
                          n_civico SMALLINT NOT NULL,
                          preferenze TEXT NOT NULL,
                          FOREIGN KEY (utente_id) REFERENCES Utente(username),
                          PRIMARY KEY (utente_id, num)
);
CREATE TABLE MetodoPagamento(
                                utente_id VARCHAR(255) NOT NULL,
                                num SMALLINT NOT NULL,
                                circuito VARCHAR(255) NOT NULL,
                                num_carta CHAR(16) NOT NULL,
                                data_scadenza DATE NOT NULL,
                                titolare_carta BIGINT NOT NULL,
                                cvv_hash VARBINARY(255) NOT NULL,
                                salt_cvv VARBINARY(16) NOT NULL,
                                FOREIGN KEY (utente_id) REFERENCES Utente(username),
                                PRIMARY KEY(utente_id, num)
);
CREATE TABLE StoricoProdotti(
                                codice_fattura VARCHAR(255) NOT NULL,
                                prodotto_id INT NOT NULL,
                                iva DOUBLE NOT NULL,
                                prezzo_unitario DOUBLE NOT NULL,
                                quantita INT NOT NULL,
                                sconto DOUBLE NOT NULL,
                                FOREIGN KEY (codice_fattura) REFERENCES Ordine(codice_fattura),
                                PRIMARY KEY (codice_fattura, prodotto_id)
);
CREATE TABLE Carrello(
                         prodotto_id INT UNSIGNED NOT NULL,
                         utente_id VARCHAR(255) NOT NULL,
                         quantita INT NOT NULL,
                         FOREIGN KEY (prodotto_id) REFERENCES Prodotto(id) ON DELETE cascade,
                         FOREIGN KEY (utente_id) REFERENCES Utente(username),
                         PRIMARY KEY(prodotto_id, utente_id)
);
CREATE TABLE Preferiti(
                          prodotto_id INT UNSIGNED NOT NULL,
                          utente_id VARCHAR(255) NOT NULL,
                          FOREIGN KEY (prodotto_id) REFERENCES Prodotto(id),
                          FOREIGN KEY (utente_id) REFERENCES Utente(username),
                          PRIMARY KEY (prodotto_id, utente_id)
);