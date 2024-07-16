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
    <link href="css/notifica.css" rel="stylesheet" type="text/css">
    <title>Storage DS/BF</title>
    <script src="js/validationAddress.js"></script>
    <script src="js/quantityCheck.js"></script>
    <script src="js/notifica.js"></script>
</head>

<body>
<jsp:include page="Header.jsp"/>
<div class="container">
    <button class="btn btn-4 btn-4d icon-arrow-left" onclick="window.location.href='./product'">
        <span>Return</span>
    </button>
    <h2 class="prodotti_carrello">Prodotti nel Carrello</h2>
    <%
        Boolean buy = (Boolean) request.getAttribute("acquistoCompletato");
        if (buy != null && buy.booleanValue()){
            request.removeAttribute("acquistoCompletato");
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
            <th>Prezzo</th>
            <th>Quantità</th>
            <th>Totale Riga</th>
            <th>Azioni</th>
        </tr>
        </thead>
        <tbody>
        <% for (CartItem item : items) {
            double prezzoUnitario = item.getItem().getPrezzo();
            double prezzoScontato = item.getItem().getPrezzoScontato();
            //funzione per calcolare il prezzo scontato
            boolean hasDiscount = prezzoScontato > 0;

            // Se la disponibilità del prodotto è zero o inferiore, rimuovilo dal carrello
            if (item.getItem().getDisponibilita() <= 0) {
                cartItem.deleteItem(item.getItem().getId());
                continue; // Passa al prossimo elemento senza generare HTML per questo prodotto
            }

            %>
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
            <td class="prezzo">
                <% if (hasDiscount){ %>
                <div class="sconto-prezzo-container">
                    <div class="sconto-visible"><%= item.getItem().getPercentualeSconto() + "%" %></div>
                    <span class="prezzo-unitario"><%= String.format("%.2f", item.getItem().getPrezzo()) %> &euro;</span>
                 </div>
            <div class="prezzo-scontato"><%= String.format("%.2f", prezzoScontato) %> &euro;</div>
            <% } else { %>
            <div><%= item.getItem().getPrezzo() %> &euro;</div>
            <% } %>
            </td>
            <td>
                <form action="carrello" method="post" class="quantity-form">
                    <input type="hidden" name="opzione" value="aggiornaQuantita">
                    <%
                        double prezzo = item.getItem().getPrezzo();
                        if (item.getItem().getPercentualeSconto() > 0) {
                            //se ha una percentuale sconto setto quella
                            //altrimenti il suo prezzo unitario
                            prezzo = item.getItem().getPrezzoScontato();
                        }
                    %>
                    <input type="hidden" name="prezzo" value="<%= prezzo %>">
                    <input type="hidden" name="id" value="<%=item.getItem().getId()%>">
                    <div class="quantity-input">
                         <button type="button" name="decrement-button" onclick="decrementQuantity(this)">-</button>
                         <input type="text" name="quantity" value="<%=item.getNumItem()%>" min="0" max="<%=item.getItem().getDisponibilita()%>"
                           oninput="checkValidityQuantity(this)" onchange="updateQuantity(this.form)" required>
                          <button type="button" name="increment-button" onclick="incrementQuantity(this)">+</button>
                    </div>
                    <div class="quantity-error" style="display:none; color: red;"></div> <!-- Messaggio di errore -->
                </form>


            </td>
            <td><%= String.format("%.2f", (hasDiscount ? prezzoScontato : prezzoUnitario) * item.getNumItem()) %> &euro;</td>
            <td><a href="carrello?opzione=delete&id=<%=item.getItem().getId()%>" class="action-button delete">X</a></td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <h3 id="spesa_totale">Spesa totale: €<%= String.format("%.2f", cartItem.getPrezzoTot()) %></h3>
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

<script>
    window.onload = function() {
        var signupSuccess = <%= session.getAttribute("signupSuccess") != null %>;
        var user = <%= session.getAttribute("userId") != null %>;

        if (signupSuccess && user) {
            showInfoNotifica("Login effettuato! Benvenuto " + "<%= session.getAttribute("userId") %>!");
            <% session.removeAttribute("signupSuccess"); %>
        }
    };
</script>

<jsp:include page="Footer.jsp"/>
</body>
</html>
