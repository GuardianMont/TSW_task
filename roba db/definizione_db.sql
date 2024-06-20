DROP DATABASE IF EXISTS tavolando;
CREATE DATABASE tavolando;
USE tavolando;

CREATE TABLE Utente(
                username VARCHAR(255) PRIMARY KEY,
                nome VARCHAR(255) NOT NULL,
                cognome VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL UNIQUE,
                n_telefono CHAR(10) NOT NULL,
                password_hash VARBINARY(255) NOT NULL,
                salt VARBINARY(16) NOT NULL,
                is_admin BOOLEAN NOT NULL DEFAULT FALSE
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
                data_scadenza CHAR(5) NOT NULL,
                titolare_carta CHAR(255) NOT NULL,
                cvv_hash VARBINARY(255) NOT NULL,
                salt_cvv VARBINARY(16) NOT NULL,
                FOREIGN KEY (utente_id) REFERENCES Utente(username),
                PRIMARY KEY (utente_id, num)
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
                immagine MEDIUMBLOB,
                percentuale_sconto INT UNSIGNED,
                is_visible BOOLEAN NOT NULL DEFAULT TRUE,
                CHECK( percentuale_sconto >= 0 AND percentuale_sconto <= 100)
);

CREATE TABLE Sconto(
                prodotto INT UNSIGNED PRIMARY KEY,
                percentuale_sconto INT UNSIGNED,
                FOREIGN KEY prodotto REFERENCES Prodotto(id)
                ON DELETE CASCADE
                ON UPDATE CASCADE,
                CHECK (percentuale_sconto >= 0 AND percentuale_sconto <=100)
);

CREATE TABLE Ordine(
                num int auto_increment Primary Key,
                utente_id VARCHAR(255) NOT NULL,
                cod_address SMALLINT NOT NULL,
                cod_method SMALLINT NOT NULL,
                data DATE NOT NULL,
                FOREIGN KEY (utente_id) REFERENCES Utente(username),
                FOREIGN KEY (utente_id, cod_address) REFERENCES Indirizzo(utente_id, num),
                FOREIGN KEY (utente_id, cod_method) REFERENCES MetodoPagamento(utente_id, num)
);

CREATE TABLE StoricoProdotti(
                codice_fattura int NOT NULL,
                prodotto_id INT UNSIGNED NOT NULL,
                iva DOUBLE NOT NULL,
                prezzo_unitario DOUBLE NOT NULL,
                quantita INT NOT NULL,
                sconto DOUBLE ,
                utente_id VARCHAR(255) NOT NULL,
                FOREIGN KEY (utente_id) REFERENCES Utente(username),
                FOREIGN KEY (codice_fattura) REFERENCES Ordine(num) ON DELETE CASCADE,
                FOREIGN KEY (prodotto_id) REFERENCES Prodotto(id) ON DELETE RESTRICT ON UPDATE RESTRICT, -- Aggiunta ON UPDATE RESTRICT
                PRIMARY KEY (codice_fattura, prodotto_id)
);

CREATE TABLE Carrello(
                prodotto_id INT UNSIGNED NOT NULL,
                utente_id VARCHAR(255) NOT NULL,
                quantita INT NOT NULL,
                FOREIGN KEY (prodotto_id) REFERENCES Prodotto(id) ON DELETE CASCADE,
                FOREIGN KEY (utente_id) REFERENCES Utente(username),
                PRIMARY KEY (prodotto_id, utente_id)
);

CREATE TABLE Preferiti(
                prodotto_id INT UNSIGNED NOT NULL,
                utente_id VARCHAR(255) NOT NULL,
                FOREIGN KEY (prodotto_id) REFERENCES Prodotto(id) ON DELETE CASCADE,
                FOREIGN KEY (utente_id) REFERENCES Utente(username),
                PRIMARY KEY (prodotto_id, utente_id)
);


INSERT INTO Prodotto (nome, descrizione, prezzo, fascia_iva, dimensioni, disponibilita, categoria, colore, immagine) VALUES ('T-Shirt', 'Maglietta bianca con logo', 15.99, 22.0, 'M', 100, 'Abbigliamento', 'Bianco', NULL);
INSERT INTO Prodotto (nome, descrizione, prezzo, fascia_iva, dimensioni, disponibilita, categoria, colore, immagine) VALUES ('Jeans', 'Jeans blu scuro', 29.99, 22.0, '32', 50, 'Abbigliamento', 'Blu', NULL);
INSERT INTO Prodotto (nome, descrizione, prezzo, fascia_iva, dimensioni, disponibilita, categoria, colore, immagine) VALUES ('Borsa', 'Borsa a tracolla nera', 39.50, 22.0, '30x20x10', 30, 'Accessori', 'Nero', NULL);
INSERT INTO Prodotto (nome, descrizione, prezzo, fascia_iva, dimensioni, disponibilita, categoria, colore, immagine) VALUES ('Scarpe', 'Sneakers bianche', 49.99, 22.0, '41', 20, 'Scarpe', 'Bianco', NULL);
INSERT INTO Prodotto (nome, descrizione, prezzo, fascia_iva, dimensioni, disponibilita, categoria, colore, immagine) VALUES ('Orologio', 'Orologio da polso nero', 79.90, 22.0, 'Unica', 10, 'Accessori', 'Nero', NULL);
INSERT INTO Prodotto (nome, descrizione, prezzo, fascia_iva, dimensioni, disponibilita, categoria, colore, immagine) VALUES ('Schiavo ', 'Mario schiavo', 1.50, 99.0, 'Unica', 1, 'Accessori', 'Nero', NULL);
