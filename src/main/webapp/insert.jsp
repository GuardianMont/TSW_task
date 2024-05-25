<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 14/05/2024
  Time: 11:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inserimento prodotti</title>
    <link rel="stylesheet" href="css/ProductStyle.css">

</head>
<body>
<a href="product" class="catalogo">catalogo</a>
<h2>Insert</h2>
<form action="product" enctype="multipart/form-data" method="post" style="" onsubmit="handleSubmit(event)">
    <input type="hidden" name="opzione" value="insert">

    <label for="nome">Nome:</label><br>
    <input id="nome" name="nome" type="text" maxlength="20" required placeholder="inserire nome"><br>

    <label for="descrizione">Descrizione:</label><br>
    <textarea class="textarea" id="descrizione" name="descrizione" maxlength="30" required placeholder="inserire descrizione"></textarea><br>

    <label for="prezzo">Prezzo:</label><br>
    <input id="prezzo" name="prezzo" type="number" min="0" value="0" required><br>

    <label for="quantita">Disponibilità</label><br>
    <input id="quantita" name="quantita" type="number" min="1" value="1" required><br>

    <label for="iva">Fascia_iva</label><br>
    <input id="iva" name="iva" type="number" min="1" value="1" required><br>

    <label for="dimensioni">Dimensioni</label><br>
    <input id="dimensioni" name="dimensioni" type="text" maxlength="40" required placeholder="inserire dimensioni"><br>

    <label for="categoria">Categoria</label><br>
    <input id="categoria" name="categoria" type="text" maxlength="40" required placeholder="inserire categoria"><br>

    <label for="colore">Colore</label><br>
    <input id="colore" name="colore" type="text" maxlength="40" required placeholder="inserire colore"><br>

    <label for="img">Image</label><br>
    <input id="img" name="img" type="file" accept="image/png, image/jpeg"><br>

    <input type="submit" value="Add"><input type="reset" value="Reset">
</form>
</body>
</html>
