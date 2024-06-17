<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inserimento prodotti</title>
    <link rel="stylesheet" href="css/insert.css">
    <script src="js/validationProduct.js"></script>
    <script src="js/specificaCategoria.js"></script>
</head>
<body>
<jsp:include page="Header.jsp"/>

<div class="container">
    <a href="product" class="catalogo">Catalogo</a>
    <h2>Inserimento Prodotti</h2>
    <form action="product" enctype="multipart/form-data" method="post" onsubmit="handleSubmit(event)">
        <input type="hidden" name="opzione" value="insert">

        <label for="nome">Nome:</label>
        <input id="nome" name="nome" type="text" maxlength="50" required placeholder="Inserire nome" pattern="^[a-zA-Z0-9\s]+$" title="nome del prodotto" >
        <span id="nome-error" class="error-message">Il campo nome è obbligatorio.</span><br>

        <label for="descrizione">Descrizione:</label>
        <textarea class="textarea" id="descrizione" name="descrizione" maxlength="500" required
                  placeholder="Inserire descrizione" pattern="^[a-zA-Z0-9\s]+$" title="descrizione del prodotto"></textarea>
        <span id="descrizione-error" class="error-message">Il campo descrizione è obbligatorio.</span><br>

        <label for="prezzo">Prezzo:</label>
        <input id="prezzo" name="prezzo" type="number" min="0" value="0" required pattern="^\d+(\.\d{1,2})?$" title="prezzo del prodotto">
        <span id="prezzo-error" class="error-message">Il campo prezzo è obbligatorio.</span><br>

        <label for="quantita">Disponibilità:</label>
        <input id="quantita" name="quantita" type="number" min="1" value="1" max="999" required>
        <span id="quantita-error" class="error-message">Il campo quantità è obbligatorio.</span><br>

        <label for="iva">Fascia IVA:</label>
        <input id="iva" name="iva" type="number" min="1" value="1" placeholder="xx"
               pattern="^([1-9]|[1-9][0-9]|100)$" title="fascia d'iva del prodotto" required>
        <span id="iva-error" class="error-message">Il campo fascia iva è obbligatorio.</span><br>

        <label for="dimensioni">Dimensioni:</label>
        <input id="dimensioni" name="dimensioni" type="text" maxlength="40" pattern="^\d+\*\d+\*\d+$"
               title="Dimensioni del prodotto" required placeholder="ww*ll*hh">
        <span id="dimensioni-error" class="error-message">Il campo dimensioni è obbligatorio.</span><br>

        <label for="categoria">Categoria:</label>
        <select id="categoria" name="categoria" required title="Categoria dei prodotti">
            <option value="" disabled selected>Selezionare una categoria</option>
            <option value="tavolo">Tavolo</option>
            <option value="attrezzatura">Attrezzatura</option>
            <option value="kit">Kit assortiti</option>
            <span id="categoria-error" class="error-message">Il campo categoria è obbligatorio.</span><br>
        </select>
        <div id="specificaTavolo">
            <label for="tipoTavolo">Tipo di Tavolo:</label>
            <select id="tipoTavolo" name="tipoTavolo">
                <option value="" disabled selected>Selezionare un tipo di tavolo</option>
                <option value="pingPong">Ping Pong</option>
                <option value="biliardo">Biliardo</option>
                <option value="multifunzione">Multifunzione</option>
            </select>
        </div>
        <div id="specificaAttrezzatura">
            <label for="tipoAttrezzatura">Tipo di Attrezzatura:</label>
            <select id="tipoAttrezzatura" name="tipoAttrezzatura">
                <option value="" disabled selected>Selezionare un tipo di attrezzatura</option>
                <option value="racchette">Racchette</option>
                <option value="stecche">Stecche</option>
                <option value="bilie">Bilie</option>
                <option value="palline">Palline</option>
                <option value="spingitori">Spingitori</option>
                <option value="dischi">dischi</option>
            </select>
        </div>

        <br><label for="colore">Colore:</label>
        <input id="colore" name="colore" type="text" maxlength="40" required placeholder="Inserire colore">
        <span id="colore-error" class="error-message">Il campo colore è obbligatorio.</span><br>

        <label for="img">Immagine:</label>
        <input id="img" name="img" type="file" accept="image/png, image/jpeg">
        <span id="Immagine-error" class="error-message">Il campo immagine è obbligatorio.</span><br>

        <div class="button-container">
            <input type="submit" value="Aggiungi">
            <input type="reset" value="Reset">
        </div>
    </form>
</div>

<jsp:include page="Footer.jsp"/>
</body>
</html>
