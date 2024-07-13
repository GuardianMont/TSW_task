<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, ec.model.product.ProductBean, java.util.Base64"%>

<%
	Collection<?> products = (Collection<?>) request.getAttribute("products");
	if (products == null) {
		response.sendRedirect("./product");
		return;
	}
%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="css/ProductView.css">
	<link rel="stylesheet" href="css/notifica.css">
	<link rel="stylesheet" href="css/Sconto.css">
	<title>Storage DS/BF</title>
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	<script src="js/functionSweetAlert.js"></script>
	<script src="js/validationProduct.js"></script>
	<script src="js/notifica.js"></script>
	<script src="js/functionFavorites.js"></script>
	<script>
		window.onload = function() {
			var signupSuccess = '<%= session.getAttribute("signupSuccess") %>' === 'true';
			var user = '<%= session.getAttribute("userId") %>' !== 'null';
			var insert = '<%= session.getAttribute("inserted") %>' === 'true';

			if (signupSuccess && user) {
				showInfoNotifica("Login effettuato! Benvenuto " + '<%= session.getAttribute("userId") %>' + "!");
				<% session.removeAttribute("signupSuccess"); %>
			}

			if (insert) {
				showInfoNotifica("Inserimento prodotto avvenuto con successo");
				<% session.removeAttribute("inserted"); %>
			}

			if ('<%= session.getAttribute("userId") %>' !== 'null') {
				//array di interi per gli identificativi dei prodotti preferiti
				loadFavorites();
			}
		};
	</script>
</head>
<body>

<jsp:include page="Header.jsp" />
<div class="generale">
	<div class="sorting-dropdown">
		<select onchange="window.location.href=this.value" class="select-sorting">
			<option value="#">Ordina per</option>
			<option value="product?sort=id">Nuove Aggiunte</option>
			<option value="product?sort=nome">Ordine alfabetico</option>
			<option value="product?sort=prezzo">Prezzo (Ascendente)</option>
			<option value="product?sort=prezzoDec">Prezzo (Decrescente)</option>
		</select>
	</div>
</div>
<%
	Object userId = request.getSession().getAttribute("userId");
	Object isAdmin = request.getSession().getAttribute("isAdmin");
	if (userId != null && isAdmin != null && (boolean) isAdmin) {
%>
<div class="button-container">
	<a href="admin/insert.jsp" id="insert-a" class="add-button">Inserire prodotto</a>
	<a href="admin/AdminOptions.jsp" id="opzioniAd-a" class="add-button">Opzioni Admin</a>
</div>
<% } %>
<%
	if (products == null || products.isEmpty()) { %>
<div>
	<div class="no-product-wrapper">
		<div class="No-product">
			<h1>Nessun risultato</h1>
			<p>prova a cercare con altre parole</p>
		</div>
	</div>
</div>
<%}%>
<div class="container">
	<%
		if (products != null && !products.isEmpty()) {
			for (Object obj : products) {
				ProductBean bean = (ProductBean) obj;
				boolean outOfStock = bean.getDisponibilita() == 0;
				double prezzoUnitario = bean.getPrezzo();
				double prezzoScontato = bean.getPrezzoScontato();
				boolean hasDiscount = prezzoScontato > 0;
	%>
	<div class="product <%= outOfStock ? "out-of-stock" : "" %>">
		<div class="product-content">
			<div class="immagini">
				<%
					String stockImg = "";
					byte[] imageData = bean.getImmagineUrl();
					if (imageData != null) {
						stockImg = Base64.getEncoder().encodeToString(imageData);
					}
				%>
				<img src="data:image/jpeg;base64,<%= stockImg %>" class="img-product" alt="<%= bean.getNome() %>" onclick="window.location.href='product?opzione=read&id=<%= bean.getId() %>'">
				<%
					if (hasDiscount) {
				%>
				<div class="sconto-visible">
					<%=bean.getPercentualeSconto() %>%
				</div>
				<% } %>
			</div>
			<h2 onclick="window.location.href='product?opzione=read&id=<%= bean.getId() %>'"><%= bean.getNome() %></h2>
			<p>
				<% if (hasDiscount) { %>
				<span class="prezzo-unitario"><%= String.format("%.2f", prezzoUnitario) %> &euro;</span>
				<span class="prezzo-scontato"><%= String.format("%.2f", prezzoScontato) %> &euro;</span>
				<% } else { %>
				<span><%= prezzoUnitario %> &euro;</span>
				<% } %>
			</p>
			<div class="button-container">
				<p>
						<%
                    if (userId != null && isAdmin != null && (boolean) isAdmin) {
                    %>
					<a href="#" class="remove-button" onclick="confirmDelete(<%= bean.getId() %>">Delete</a> <br>
					<a href="product?opzione=show&id=<%= bean.getId() %>" class="add-button">Modifica</a>
						<%
                    }
                    if (bean.getDisponibilita() > 0) {
                    %>
					<a href="carrello?opzione=add&id=<%= bean.getId() %>" class="add-button">Aggiungi al carrello</a> <br>
						<%
                    } else {
                    %>
				<div class="out-of-stock-message">
					<img src="uploadFile/erroreAttentionIcon.png" alt="Info Icon" width="15" height="15">
					<span id="out-of-stock-message-text">Esaurito</span>
				</div>
				<br>
				<%
					}
				%>
				<% if(userId!=null){
					//l'immgine dei preferiti viene visualizzata solo in caso vi sia un utente loggato%>
				<img src="uploadFile/favorites_32.png" alt="Favorite Icon" id="favorite-icon-<%= bean.getId() %>"
					 class="favorite not-added" onclick="toggleFavorite(<%= bean.getId() %>)">

				<%}%>
				</p>
			</div>
		</div>
	</div>
	<%
			}
		}
	%>
</div>
<jsp:include page="Footer.jsp" />

<script>
	// JavaScript per gestire il click sul pulsante
	var dropdownBtn = document.querySelector(".dropdown-btn");
	if (dropdownBtn) {
		dropdownBtn.addEventListener("click", function() {
			this.parentElement.classList.toggle("active");
		});
	}
</script>

</body>
</html>
