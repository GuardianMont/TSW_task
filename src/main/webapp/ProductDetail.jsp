<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.Base64, ec.model.product.ProductBean"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dettaglio prodotto</title>
    <link href="css/ProductStyle.css" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="Header.jsp"/>

<div class="product-container">
    <a href="./product" class="return-button">Return</a>
    <%
        ProductBean product = (ProductBean) request.getAttribute("product");
        if (product != null) {
            boolean outOfStock = product.getDisponibilita() == 0;
            double prezzoUnitario = product.getPrezzo();
            double prezzoScontato = product.getPrezzoScontato();
            //funzione per calcolare il prezzo scontato
            boolean hasDiscount = prezzoScontato > 0;
    %>
    <div class="product-content <%= outOfStock ? "out-of-stock" : "" %>">
        <div class="product-image">
            <br>
            <%
                String stockImg = "";
                byte[] imageData = product.getImmagineUrl();
                if (imageData != null) {
                    stockImg = Base64.getEncoder().encodeToString(imageData);
                }
            %>
            <img src="data:image/jpeg;base64,<%= stockImg %>" alt="no immagine" />
        </div>
        <div class="product-details">
            <h1>
                <%
                    if (outOfStock) {
                %>
                <div class="out-of-stock-message">
                    <img src="uploadFile/erroreAttentionIcon.png" alt="Info Icon" width="15" height="15">
                    <span id="out-of-stock-message-text">Esaurito</span>
                </div>
                <%
                    }
                %>
                <%= product.getNome() %>
            </h1>
            <p><%= product.getDescrizione() %></p>
        </div>
        <div class="product-price">
            <% if (hasDiscount){ %>
            <div class="sconto-prezzo-container">
                <div class="sconto-visible"><%= product.getPercentualeSconto() + "%" %></div>
                <span class="prezzo-unitario"><%= String.format("%.2f", product.getPrezzo()) %> &euro;</span>
            </div>
            <h2 class="prezzo-scontato"><%= String.format("%.2f", prezzoScontato) %> &euro;</h2>
            <% } else { %>
            <h2><%= product.getPrezzo() %> &euro;</h2>
            <% } %>
            <%
                if (product.getDisponibilita() > 0) {
            %>
            <a href="carrello?opzione=add&id=<%= product.getId() %>" class="add-to-cart-button">Add to Cart</a>
            <%
                }
            %>
        </div>
    </div>

    <!-- Informazioni aggiuntive del prodotto -->
    <div class="product-info-table">
        <table>
            <tr>
                <th>Categoria:</th>
                <td><%= product.getCategoria() %></td>
            </tr>
            <tr>
                <th>Colore:</th>
                <td><%= product.getColore() %></td>
            </tr>
            <tr>
                <th>Dimensioni:</th>
                <td><%= product.getDimensioni() %></td>
            </tr>
        </table>
    </div>
    <%
        }
    %>
</div>

<jsp:include page="Footer.jsp"/>
</body>
</html>
