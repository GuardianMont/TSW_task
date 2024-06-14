<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (request.getSession().getAttribute("userId") == null ){
        response.sendRedirect(request.getContextPath() + "/login_signup.jsp");
        return;
    }
    if (request.getSession().getAttribute("cart")==null){
        response.sendRedirect(request.getContextPath() + "/Cart.jsp");
        return;
    }
%>
<html>
<head>
    <title>Indirizzo di Spedizione</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="js/validationAddress.js"></script>
    <script src="js/validationPayMathods.js"></script>
    <script src="js/loadAddressPay.js"></script>
    <link rel="stylesheet" href="css/notificaADPM.css">
    <link rel="stylesheet" href="css/Payment.css">
    <link rel="stylesheet" href="css/confermaAcquisto.css">
</head>
<body>
<jsp:include page="Header.jsp"/>
<div class="notification" id="notificationAD">
    <img src="uploadFile/erroreAttentionIcon.png" alt="Info Icon" width="20" height="20">
    <span id="notification-textAD"></span>
</div>
<div class="notification" id="notificationPM">
    <img src="uploadFile/erroreAttentionIcon.png" alt="Info Icon" width="20" height="20">
    <span id="notification-textPM"></span>
</div>

    <!--action="CheckoutServlet" method="post"-->
<div class="section_1">
    <h2>Indirizzo di Spedizione</h2>
    <div id="shipping-addresses">
        <!--immetto le informazioni con ajax -->
    </div>
    <div id = "add-button" class="">
        <button class="add-button add-methods-button" onclick="aggiungiIndirizzo()">Aggiungi Nuovo Indirizzo</button>
    </div>
    <div id="remove-button" class="hidden">
        <button class="remove-button" onclick="removeForm()">X</button>
    </div>
    <div id="new-address-form" class="hidden">
        <!--form per l'aggiunta di un nuovo indirizzo celato se l'utente non clicca il bottone-->
        <form id="addressForm" action="AddressManagement" method="POST">
            <input type="hidden" name="opzione" value="add">

            <label for="via">Via:</label><br>
            <input id="via" name="via" type="text" maxlength="20"  placeholder="Via Muni..."><br>
            <span id="via-error" class="error-message">Il campo 'Via' è obbligatorio.</span><br>

            <label for="n_civico">Numero civico </label><br>
            <input id="n_civico" name="n_civico" type="text" maxlength="4" ><br>
            <span id="n_civico-error" class="error-message">Inserisci un numero civico valido.</span><br>

            <label for="preferenze">Descrizione:</label><br>
            <textarea class="textarea" id="preferenze" name="preferenze" maxlength="30"  placeholder="inserire preferenze di spedizione"></textarea><br>

            <label for="cap">Cap:</label><br>
            <input id="cap" name="cap" type="text" maxlength="5" placeholder="00000"><br>
            <span id="cap-error" class="error-message">Inserisci un CAP valido (5 cifre).</span><br>

            <label for="citta">Città</label><br>
            <input id="citta" name="citta" type="text" placeholder="città" > <br>
            <span id="citta-error" class="error-message">Il campo 'Città' è obbligatorio.</span><br>

            <label for="provincia">Provincia</label><br>
            <input id="provincia" name="provincia" type="text" maxlength="2" ><br>
            <span id="provincia-error" class="error-message">Inserisci una provincia valida (2 lettere).</span><br>

            <input type="submit" value="Add Adress" class="add-address-button"><input type="reset" value="Reset">
        </form>
    </div>
</div>
<div class="section_2">
    <h2>Metodo di Pagamento</h2>
    <div id="shipping-payment">
        <!--immetto le informazioni con ajax -->
    </div>
    <div id="add-pay-button" class="">
        <button class="add-button add-methods-button" onclick="aggiungiPayMethods()">Aggiungi Nuovo Metodo di pagamento</button>
    </div>
    <div id ="remove-pay-form" class="hidden">
        <button class="remove-button" onclick="removePayForm()">X</button>
    </div>
    <div id="new-payMethod-form" class="hidden">
        <!--form per l'aggiunta di un nuovo metodo di pagamento celato se l'utente non clicca il bottone-->
        <form id="payMethodsForm" action="payMethodsManager" method="post">
            <input type="hidden" name="opzione" value="add">

            <label for="NumeroCarta">Numero Carta:</label><br>
            <input id="NumeroCarta" name="NumeroCarta" type="text" maxlength="16"  placeholder="numero carta"><br>
            <span id="NumeroCartaError" class="error-message">Il campo Numero Carta è obbligatorio.</span><br>

            <label for="meseScadenza">Data scadenza:</label><br>
            <input id="meseScadenza" name="meseScadenza" type="text" maxlength="2" placeholder="MM">

            <label for="annoScadenza">/</label><br>
            <input id="annoScadenza" name="annoScadenza" type="text" maxlength="2" placeholder="YY"><br>
            <span id="dataScadenzaError" class="error-message">Inserisci una Data Scadenza valida (MMYY).</span><br>

            <label for="cvv">Cvv:</label><br>
            <textarea id="cvv" name="cvv" maxlength="3" placeholder="000"></textarea><br>
            <span id="CvvError" class="error-message">Inserisci un CVV valido (3 cifre).</span><br>

            <label for="circuito">Circuito:</label><br>
            <input id="circuito" name="circuito" type="text" maxlength="20" placeholder="visa-..." ><br>
            <span id="circuitoError" class="error-message">Il campo 'Circuito' è obbligatorio.</span><br>

            <label for="titolareCarta">Titolare Carta:</label><br>
            <input id="titolareCarta" name="titolareCarta" type="text" placeholder="nome cognome" > <br>
            <span id="titolareError" class="error-message">Il campo Titolare Carta è obbligatoria.</span><br>

            <input type="submit" value="Add Methods" class="add-methods-button"><input type="reset" value="Reset">
        </form>
    </div>
</div>

<form id="check-out-form" action="CheckoutServlet" method="post">
    <input type="submit" value="Procedi al checkOut" class="proceed-button">
</form>
<jsp:include page="Footer.jsp"/>
</body>
</html>
