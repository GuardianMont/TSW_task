<%@ page import="ec.model.product.ProductBean" %>
<%@ page import="java.util.Base64" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dettaglio prodotto</title>
    <link rel="stylesheet" href="css/update.css">
</head>
<body>
<jsp:include page="Header.jsp"/>
<%
    ProductBean product = (ProductBean) request.getAttribute("product");
    if (product != null) {
%>
<div class="container">
    <h1>Prodotto <%= product.getNome() %> con id: <%= product.getId() %></h1>
    <%
        String stockImg = "";
        byte[] imageData = product.getImmagineUrl();
        if (imageData != null) {
            stockImg = Base64.getEncoder().encodeToString(imageData);
        }
    %>
    <img src="data:image/jpeg;base64,<%= stockImg %>" width="400px" height="auto" alt="no immagine" />

    <form action="product" enctype="multipart/form-data" method="post" class="form">
        <input type="hidden" name="opzione" value="update">
        <input type="hidden" name="identificatore" value="<%=product.getId()%>">

        <label for="nome">Nome:</label><br>
        <input id="nome" name="nome" type="text" maxlength="20" required value="<%=product.getNome()%>"><br>

        <label for="descrizione">Descrizione:</label><br>
        <textarea id="descrizione" name="descrizione" maxlength="100" rows="3" required ><%=product.getDescrizione()%></textarea><br>

        <label for="prezzo">Prezzo:</label><br>
        <input id="prezzo" name="prezzo" type="number" min="0" required value="<%=product.getPrezzo()%>"><br>

        <label for="quantita">Disponibilità</label><br>
        <input id="quantita" name="quantita" type="number" min="0" required value="<%=product.getDisponibilita()%>"><br>

        <label for="iva">Fascia_iva</label><br>
        <input id="iva" name="iva" type="number" min="1" required value="<%=product.getFasciaIva()%>"><br>

        <label for="dimensioni">Dimensioni</label><br>
        <input id="dimensioni" name="dimensioni" type="text" maxlength="40" required value="<%=product.getDimensioni()%>"><br>

        <label for="categoria">Categoria</label><br>
        <input id="categoria" name="categoria" type="text" maxlength="40" required value="<%=product.getCategoria()%>"><br>

        <label for="colore">Colore</label><br>
        <input id="colore" name="colore" type="text" maxlength="40" value="<%=product.getColore()%>" required ><br>

        <label for="img">Immagine:</label><br>
        <input id="img" name="img" type="file" accept="image/png, image/jpeg"><br>

        <input type="submit" value="Salva modifiche"><input type="reset" value="Reset">
    </form>
</div>
<% } %>

<a href="./product" class="return-link">Torna alla lista dei prodotti</a>

<jsp:include page="Footer.jsp"/>
</body>
</html>
