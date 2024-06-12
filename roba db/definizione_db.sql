DROP database IF EXISTS tavolando;
CREATE DATABASE tavolando;
USE tavolando;

CREATE TABLE Ordine(
                       codice_fattura int auto_increment PRIMARY KEY,
                       utente_id VARCHAR(255) NOT NULL,
                       cod_address smallint not null,
                       cod_method smallint not null,
                       data DATE NOT NULL,
                       foreign key(utente_id) references Utente(username),
                       foreign key(utente_id, cod_address) references Indirizzo( utente_id, num),
                       foreign Key (utente_id, cod_method) references MetodoPagamento (utente_id, num)

);
CREATE TABLE Utente(
                       username VARCHAR(255) PRIMARY KEY,
                       nome VARCHAR(255) NOT NULL,
                       cognome VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL unique,
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
                          preferenze TEXT,
                          FOREIGN KEY (utente_id) REFERENCES Utente(username),
                          PRIMARY KEY (utente_id, num)
);
CREATE TABLE MetodoPagamento(
                                utente_id VARCHAR(255) NOT NULL,
                                num SMALLINT NOT NULL,
                                circuito VARCHAR(255) NOT NULL,
                                num_carta CHAR(16) NOT NULL,
                                data_scadenza CHAR (5) NOT NULL,
                                titolare_carta CHAR(255) NOT NULL,
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
                                utente_id VARCHAR(255) not null,
                                FOREIGN KEY (utente_id)REFERENCES Utente(username),
                                FOREIGN KEY (codice_fattura) REFERENCES Ordine(codice_fattura),
                                FOREIGN KEY (prodotto_id) REFERENCES Prodotto(id) ON DELETE RESTRICT,
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
                          FOREIGN KEY (prodotto_id) REFERENCES Prodotto(id) ON DELETE CASCADE,
                          FOREIGN KEY (utente_id) REFERENCES Utente(username),
                          PRIMARY KEY (prodotto_id, utente_id)
);
