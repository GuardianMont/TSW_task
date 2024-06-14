<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inserimento prodotti</title>
    <link rel="stylesheet" href="css/insert.css">
</head>
<body>
<jsp:include page="Header.jsp"/>

<div class="container">
    <a href="product" class="catalogo">Catalogo</a>
    <h2>Inserimento Prodotti</h2>
    <form action="product" enctype="multipart/form-data" method="post" onsubmit="handleSubmit(event)">
        <input type="hidden" name="opzione" value="insert">

        <label for="nome">Nome:</label>
        <input id="nome" name="nome" type="text" maxlength="20" required placeholder="Inserire nome">

        <label for="descrizione">Descrizione:</label>
        <textarea class="textarea" id="descrizione" name="descrizione" maxlength="30" required placeholder="Inserire descrizione"></textarea>

        <label for="prezzo">Prezzo:</label>
        <input id="prezzo" name="prezzo" type="number" min="0" value="0" required>

        <label for="quantita">Disponibilità:</label>
        <input id="quantita" name="quantita" type="number" min="1" value="1" required>

        <label for="iva">Fascia IVA:</label>
        <input id="iva" name="iva" type="number" min="1" value="1" required>

        <label for="dimensioni">Dimensioni:</label>
        <input id="dimensioni" name="dimensioni" type="text" maxlength="40" required placeholder="Inserire dimensioni">

        <label for="categoria">Categoria:</label>
        <input id="categoria" name="categoria" type="text" maxlength="40" required placeholder="Inserire categoria">

        <label for="colore">Colore:</label>
        <input id="colore" name="colore" type="text" maxlength="40" required placeholder="Inserire colore">

        <label for="img">Immagine:</label>
        <input id="img" name="img" type="file" accept="image/png, image/jpeg">

        <div class="button-container">
            <input type="submit" value="Aggiungi">
            <input type="reset" value="Reset">
        </div>
    </form>
</div>

<jsp:include page="Footer.jsp"/>
</body>
</html>
