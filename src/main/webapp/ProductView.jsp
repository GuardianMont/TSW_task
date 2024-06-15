<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>

<%
	Collection<?> products = (Collection<?>) request.getAttribute("products");
	if(products == null) {
		response.sendRedirect("./product");
		return;
	}
%>

<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*,ec.model.product.ProductBean"%>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="css/ProductView.css">
	<link rel="stylesheet" href="css/notifica.css">
	<title>Storage DS/BF</title>

	<script src="js/validationProduct.js"></script>
	<script src="js/loginMessage.js"></script>
	<script>
		window.onload = function() {
			var signupSuccess = <%= session.getAttribute("signupSuccess") != null %>;
			var user = <%= session.getAttribute("userId") != null %>;

			if (signupSuccess && user) {
				showNotification("Login effettuato! Benvenuto " + "<%= session.getAttribute("userId") %>!");
				<% session.removeAttribute("signupSuccess"); %>
			}
		};
	</script>
</head>

<body>

<jsp:include page="Header.jsp"/>
<div class="notification" id="notification">
	<img src="uploadFile/IconInfo.png" alt="Info Icon" width="20" height="20" >
	<span id="notification-text"></span>
</div>
<div class="generale">
	<div class="sorting-dropdown">
		<select onchange="window.location.href=this.value" class="select-sorting">
			<option value="#">Ordina per</option>
			<option value="product?sort=id">ID</option>
			<option value="product?sort=nome">Nome</option>
			<option value="product?sort=prezzo">Prezzo (Ascendente)</option>
			<option value="product?sort=prezzoDec">Prezzo (Decrescente)</option>
		</select>
	</div>
</div>

<div class="container">
	<%
		if (products != null && products.size() != 0) {
			Iterator<?> it = products.iterator();
			while (it.hasNext()) {
				ProductBean bean = (ProductBean) it.next();
				boolean outOfStock = bean.getDisponibilita() == 0;
	%>

	<div class="product <%=outOfStock ?  "out-of-stock" : "" %>" >
		<div class="product-content">
			<div class="immagini">
				<%
					String stockImg = "";
					byte[] imageData = bean.getImmagineUrl();
					if (imageData != null) {
						stockImg = Base64.getEncoder().encodeToString(imageData);
					}
				%>

				<img src="data:image/jpeg;base64,<%= stockImg %>" class="img-product" alt="<%=bean.getNome()%>" onclick="window.location.href='product?opzione=read&id=<%=bean.getId()%>'">
			</div>
			<h2><%=bean.getNome()%></h2>
			<p><%=bean.getPrezzo()%></p>
			<div class="button-container">
				<p>
					<%
						if (request.getSession().getAttribute("userId")!=null){
					%>
					<a href="product?opzione=delete&id=<%=bean.getId()%>" class="remove-button">Delete</a> <br>
					<a href="product?opzione=show&id=<%=bean.getId()%>" class="add-button">Modifica</a>
						<%
							}
						%>


						<%
                        if (bean.getDisponibilita() > 0) {
                    %>
					<a href="carrello?opzione=add&id=<%=bean.getId()%>" class="add-button">Aggiungi al carrello</a> <br>
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
				</p>
			</div>
		</div>
	</div>

	<%
		}
	%>
</div>
<% } else { %>
<tr>
	<td colspan="6">No products available</td>
</tr>
<% } %>

<a href="insert.jsp" class="inserimento">Inserire prodotto</a>

<script>
	// JavaScript per gestire il click sul pulsante
	document.querySelector(".dropdown-btn").addEventListener("click", function() {
		this.parentElement.classList.toggle("active");
	});
</script>

<jsp:include page="Footer.jsp"/>
</body>
</html>
