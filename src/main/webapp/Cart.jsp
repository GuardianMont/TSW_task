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
    <link href="Cart.css" rel="stylesheet" type="text/css">
    <title>Storage DS/BF</title>
</head>

<body>
<a href="product" class="catalogo">catalogo</a>
<h2 class="prodotti_carrello">Prodotti carrello</h2>
<%
    Boolean buy = (Boolean) request.getAttribute("acquistoCompletato");
    if (buy != null && buy.booleanValue()){
%>
<p>Acquisto completato con successo</p>
<%
    }
%>
<%
    if (cartItem != null && !cartItem.isEmpty()) {
        List <CartItem> items = cartItem.getItem_ordinati();
%>
<table border="1" class="tabella">
    <tr>
        <th>ID </th>
        <th>img</th>
        <th>Nome </th>
        <th>Spesa totale </th>
        <th>Action</th>
    </tr>
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
        <td><img  src="C:\Users\mario\OneDrive\Desktop\prova\download.jpeg;base64,<%= stockImg %>"  width=400px height=auto alt="no immagine" ></td>
        <td><%=item.getItem().getNome()%></td>
        <td><%=item.getItem().getPrezzo() + "*" + item.getNumItem() + "=" + item.prezzoAllItem()%> </td>
        <td><a href="carrello?opzione=delete&id=<%=item.getItem().getId()%>">Delete from cart</a><br>
            <%//id del prodotto che vogliamo andare a cancellare %>
            <a href="carrello?opzione=decrement&id=<%=item.getItem().getId()%>">Remove 1</a><br>
            <a href="carrello?opzione=increment&id=<%=item.getItem().getId()%>">Add 1</a>
        </td>
    </tr>
    <%
        } %>
    <h3 class="spesa_totale">Spesa totale <%=cartItem.getPrezzoTot()%></h3>
    <form action="carrello" method="post">
        <input type="hidden" name="opzione" value="acquisto">
        <input type="submit" value="ACQUISTA" class="acquista">
    </form>
    <%
    } else {
    %>
    <tr>
        <td colspan="6">No products in CART</td>
    </tr>
    <%
        }
    %>
</body>
</html>
