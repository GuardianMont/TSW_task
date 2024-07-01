<%@ page import="ec.model.product.ProductBean" %>
<%@ page import="java.util.Base64" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dettaglio prodotto</title>
    <link rel="stylesheet" href="../css/insert.css">
    <script src="../js/validationProduct.js"></script>
    <script src="../js/specificaCategoria.js"></script>
</head>
<body>
<jsp:include page="../Header.jsp"/>
<%
    ProductBean product = (ProductBean) request.getAttribute("product");
    if (product != null) {
%>
<div class="container">
        <a href="./product" class="catalogo">Catalogo</a>
        <h2>Prodotto <%= product.getNome() %> con id: <%= product.getId() %></h2>
    <%
        String stockImg = "";
        byte[] imageData = product.getImmagineUrl();
        if (imageData != null) {
            stockImg = Base64.getEncoder().encodeToString(imageData);
        }
    %>
    <img src="data:image/jpeg;base64,<%= stockImg %>" width="400px" height="auto" alt="no immagine" />

    <form action="product" enctype="multipart/form-data" method="post" class="form" onsubmit="handleSubmit(event)">
        <input type="hidden" name="opzione" value="update">
        <input type="hidden" name="identificatore" value="<%=product.getId()%>">

        <label for="nome">Nome:</label><br>
        <input id="nome" name="nome" type="text" maxlength="50"
               required pattern="^[a-zA-Z0-9\s]+$" value="<%=product.getNome()%>"><br>
        <span id="nome-error" class="error-message">Il campo nome è obbligatorio.</span><br>

        <label for="descrizione">Descrizione:</label><br>
        <textarea id="descrizione" name="descrizione" maxlength="500" rows="5"
                  required pattern="^[a-zA-Z0-9\s]+$"><%=product.getDescrizione()%></textarea><br>
        <span id="descrizione-error" class="error-message">Il campo descrizione è obbligatorio.</span><br>

        <label for="prezzo">Prezzo:</label><br>
        <input id="prezzo" name="prezzo" type="text"
               pattern="^\d+(\.\d{1,2})?$" required value="<%=product.getPrezzo()%>"><br>
        <span id="prezzo-error" class="error-message">Il campo prezzo è obbligatorio.</span><br>

        <label for="quantita">Disponibilità</label><br>
        <input id="quantita" name="quantita" type="number" min="1" max="999"
               required value="<%=product.getDisponibilita()%>"><br>
        <span id="quantita-error" class="error-message">Il campo quantità è obbligatorio.</span><br>

        <label for="iva">Fascia_iva</label><br>
        <input id="iva" name="iva" type="text"  required pattern="^(?:[1-9]|[1-9][0-9])$"
               value="<%=product.getFasciaIva()%>"><br>
        <span id="iva-error" class="error-message">Il campo fascia iva è obbligatorio.</span><br>

        <label for="dimensioni">Dimensioni</label><br>
        <input id="dimensioni" name="dimensioni" type="text" maxlength="40"
               pattern="^\d+\*\d+(\*\d+)?$" required value="<%=product.getDimensioni()%>"><br>
        <span id="dimensioni-error" class="error-message">Il campo dimensioni è obbligatorio.</span><br>

        <label for="sconto">Sconto:</label>
        <input id ="sconto" name="sconto" type="number" min="0" max="99" value="<%=product.getPercentualeSconto()%>"
               title="sconto da applicare al prodotto" required>
        <span id="sconto-error" class="error-message">Il campo sconto è obbligatorio</span>

        <select id="categoria" name="categoria" required title="Categoria dei prodotti">
            <option value="<%=product.getCategoria()%>" selected><%=product.getCategoria()%></option>
            <option value="tavolo">Tavolo</option>
            <option value="attrezzatura">Attrezzatura</option>
            <option value="kit">Kit assortiti</option>
        </select>
        <span id="categoria-error" class="error-message">Il campo categoria è obbligatorio.</span><br>
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
        <label for="colore">Colore</label><br>
        <input id="colore" name="colore" type="text" pattern="^[a-zA-Z0-9\s]+$"
               maxlength="40" value="<%=product.getColore()%>" required ><br>
        <span id="colore-error" class="error-message">Il campo colore è obbligatorio.</span><br>

        <label for="img">Immagine:</label><br>
        <input id="img" name="img" type="file" accept="image/png, image/jpeg"><br>
        <span id="Immagine-error" class="error-message">Il campo immagine deve essere valido</span><br>

        <div class="button-container">
            <input type="submit" value="Aggiungi">
            <input type="reset" value="Reset">
        </div>
    </form>
</div>
<% } %>

<jsp:include page="../Footer.jsp"/>
</body>
</html>
