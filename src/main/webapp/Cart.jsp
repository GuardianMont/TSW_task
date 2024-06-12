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
    <script>
        function checkValidityQuantity(input) {
            var value = parseInt(input.value);
            if (value < parseInt(input.min)) {
                input.value = input.min; // Imposta il valore al minimo ovvero 1
            }
            if (value > parseInt(input.max)) {
                input.value = input.max; // Imposta il valore al massimo (in base al prodoott)
            }
        }
        function incrementQuantity(button) {
            var input = button.previousElementSibling;
            var max = parseInt(input.max);
            var currentValue = parseInt(input.value);
            if (currentValue < max) {
                input.value = currentValue + 1;
                updateQuantity(input.form);
            }
        }

        function decrementQuantity(button) {
            var input = button.nextElementSibling;
            var min = parseInt(input.min);
            var currentValue = parseInt(input.value);
            if (currentValue > min) {
                input.value = currentValue - 1;
                updateQuantity(input.form);
            }
        }

        function updateQuantity(form) {
            form.submit();
        }
    </script>
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
            <th>Nome</th>
            <th>Immagine</th>
            <th>prezzo</th>
            <th>quantità</th>
            <th>Azioni</th>
        </tr>
        </thead>
        <tbody>
        <% for (CartItem item : items) { %>
        <tr>
            <td><%=item.getItem().getNome()%></td>
            <%
                String stockImg = "";
                byte[] imageData = item.getItem().getImmagineUrl();
                if (imageData != null) {
                    stockImg = Base64.getEncoder().encodeToString(imageData);
                }
            %>
            <td><img src="data:image/jpeg;base64,<%= stockImg %>" class="img-product" alt="Immagine prodotto"></td>
            <td><%=item.getItem().getPrezzo()%></td>
            <td>
                <form action="carrello" method="post" class="quantity-form">
                    <input type="hidden" name="opzione" value="aggiornaQuantita">
                    <input type="hidden" name="id" value="<%=item.getItem().getId()%>">
                    <button type="button" onclick="decrementQuantity(this)">-</button>
                    <input type="text" name="quantity" value="<%=item.getNumItem()%>" min="0" max="<%=item.getItem().getDisponibilita()%>" oninput="checkValidityQuantity(this)" onchange="updateQuantity(this.form)">
                    <button type="button" onclick="incrementQuantity(this)">+</button>
                </form>
            </td>
            <td><a href="carrello?opzione=delete&id=<%=item.getItem().getId()%>" class="action-button delete">X</a></td>
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
