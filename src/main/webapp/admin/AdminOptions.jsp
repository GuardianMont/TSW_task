<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>AdminOptions</title>
    <script src="${pageContext.request.contextPath}/js/notifica.js"></script>
    <script src="${pageContext.request.contextPath}/js/adminPageOptions.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/notifica.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/AdminOptions.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Ordini.css">
</head>
<body>
<jsp:include page="../Header.jsp"/>

<div class="container">
    <h1>Admin Options</h1>
    <div id="admin-options" class="button-container">
        <button onclick="viewOrders()">Visualizza tutti gli ordini</button>
        <button onclick="viewUsers()">Gestione Utenti</button>
        <button onclick="window.location.href='../product'">Gestione prodotti</button>
        <button onclick="window.location.href='../profileServlet'">Pagina utente admin</button>
    </div>
</div>

<div class="userView hide" id="userView">
    <div class="user-view">
        <h1>Utenti Registrati</h1>
        <div class="Utenti-info table-container">
            <table class="tabella">
                <thead>
                <tr>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Nome</th>
                    <th>Cognome</th>
                    <th>Numero di Telefono</th>
                    <th>Ruolo</th>
                    <th>View</th>
                </tr>
                </thead>
                <tbody id="UtentiInfo"></tbody>
            </table>
        </div>
    </div>
</div>

<div class="ordiniEffettuati hide" id="ordiniEffettuati">
    <div class="generale">
        <div class="sorting-dropdown">
            <select onchange="orderOrdini(this.value)" class="select-sorting" id="sortingDropdown">
                <option value="#">Ordina per</option>
                <option value="data">Dal meno recente</option>
                <option value="dataDESC">Dal più recente</option>
            </select>
        </div>
        <div class="date-range">
            <label for="startDate">Da:</label>
            <input type="date" id="startDate" name="startDate">
            <label for="endDate">A:</label>
            <input type="date" id="endDate" name="endDate" >
            <script>
                // Ottieni la data odierna
                var today = new Date();

                // Formatta la data nel formato yyyy-mm-dd
                var dd = String(today.getDate()).padStart(2, '0');
                var mm = String(today.getMonth() + 1).padStart(2, '0'); // Gennaio è 0!
                var yyyy = today.getFullYear();

                var formattedDate = yyyy + '-' + mm + '-' + dd;

                // Imposta il valore dell'input date
                document.getElementById('endDate').value = formattedDate;
            </script>
            <button onclick="filterOrdersByDate()">Search</button>
        </div>
    </div>
    <div id="checkOutOrdini">
        <h1>Ordini effettuati</h1>
        <!-- li immetto con ajax -->
    </div>
</div>

<jsp:include page="../Footer.jsp"/>
</body>
</html>
