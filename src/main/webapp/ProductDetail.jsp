<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" import="java.util.Base64,ec.model.product.ProductBean"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Dettaglio prodotto</title>
    <link href="css/ProductStyle.css" rel="stylesheet" type="text/css">
    <link href="css/ProductDetailStyle.css" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="Header.jsp"/>
<div class="product-container">
    <%
        ProductBean product = (ProductBean) request.getAttribute("product");
        if (product != null) {
    %>
    <h1>Prodotto <%= product.getNome() %></h1>
    <%
        String stockImg = "";
        byte[] imageData = product.getImmagineUrl();
        if (imageData != null) {
            stockImg = Base64.getEncoder().encodeToString(imageData);
        }
    %>
    <img src="data:image/jpeg;base64,<%= stockImg %>" alt="no immagine" />
    <table>
        <tr>
            <th>ID</th>
            <th>Descrizione</th>
            <th>Quantità</th>
            <th>Prezzo</th>
            <th>Colore</th>
        </tr>
        <tr>
            <td data-label="ID"><%= product.getId() %></td>
            <td data-label="Descrizione"><%= product.getDescrizione() %></td>
            <td data-label="Quantità"><%= product.getDisponibilita() %></td>
            <td data-label="Prezzo"><%= product.getPrezzo() %></td>
            <td data-label="Colore"><%=product.getColore()%></td>
        </tr>
    </table>
    <a href="carrello?opzione=add&id=<%=product.getId()%>">Add to Cart</a><br>
    <a href="./product">Return</a>
    <%
        }
    %>
</div>
<jsp:include page="Footer.jsp"/>
</body>
</html>
