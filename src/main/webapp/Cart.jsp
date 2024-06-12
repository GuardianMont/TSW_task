<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%
    ShoppingCart cartItem = (ShoppingCart) session.getAttribute("cart");
    if(cartItem == null) {
        response.sendRedirect("./carrello");
        return;
    }
%>

<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*"%>
<%@ page import="ec.model.cart.CartItem" %>
<%@ page import="ec.model.cart.ShoppingCart" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="css/Cart.css" rel="stylesheet" type="text/css">
    <title>Storage DS/BF</title>
</head>

<body>
<jsp:include page="Header.jsp"/>

<div class="container">
    <a href="product" class="catalogo">Catalogo</a>
    <h2 class="prodotti_carrello">Prodotti nel Carrello</h2>
    <%
        Boolean buy = (Boolean) request.getAttribute("acquistoCompletato");
        if (buy != null && buy.booleanValue()){
    %>
    <p class="success-message">Acquisto completato con successo</p>
    <%
        }
    %>
    <%
        if (cartItem != null && !cartItem.isEmpty()) {
            List<CartItem> items = cartItem.getItem_ordinati();
    %>
    <table class="tabella">
        <thead>
        <tr>
            <th>ID</th>
            <th>Immagine</th>
            <th>Nome</th>
            <th>Azioni</th>
        </tr>
        </thead>
        <tbody>
        <% for (CartItem item : items) { %>
        <tr>
            <td><%=item.getItem().getId()%></td>
            <%
                String stockImg = "";
                byte[] imageData = item.getItem().getImmagineUrl();
                if (imageData != null) {
                    stockImg = Base64.getEncoder().encodeToString(imageData);
                }
            %>
            <td><img src="data:image/jpeg;base64,<%= stockImg %>" class="img-product" alt="Immagine prodotto"></td>
            <td><%=item.getItem().getNome()%></td>
            <td>
                <a href="carrello?opzione=delete&id=<%=item.getItem().getId()%>" class="action-button delete">Rimuovi dal carrello</a>
                <a href="carrello?opzione=decrement&id=<%=item.getItem().getId()%>" class="action-button decrement">Rimuovi 1</a>
                <a href="carrello?opzione=increment&id=<%=item.getItem().getId()%>" class="action-button increment">Aggiungi 1</a>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <h3 class="spesa_totale">Spesa totale: €<%=cartItem.getPrezzoTot()%></h3>
    <form action="carrello" method="post" class="acquista-form">
        <input type="hidden" name="opzione" value="acquisto">
        <input type="submit" value="ACQUISTA" class="acquista-button">
    </form>
    <%
    } else {
    %>
    <div class="no_product">
        <img src="uploadFile/piangi.jpg" alt="No product image" class="no-product-image">
        <p>Non ci sono prodotti nel carrello</p>
    </div>
    <%
        }
    %>
</div>

<jsp:include page="Footer.jsp"/>
</body>
</html>
