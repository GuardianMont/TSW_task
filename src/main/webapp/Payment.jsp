<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (request.getSession().getAttribute("userId") == null){
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login_signup.jsp");
        dispatcher.forward(request, response);
    }
%>
<html>
<head>
    <title>Indirizzo di Spedizione</title>
    <!-- questo va messo nel css relativo alla spedizione-->
    <style>
        .hidden {
            display: none;
        }
        .error {
            border-color: red;
        }
        .error-message {
            color: red;
            display: none;
        }
    </style>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="js/validationAddress.js"></script>
    <script src="js/validationPayMathods.js"></script>
    <script src="js/loadAddressPay.js"></script>
</head>
<body>
<jsp:include page="Header.jsp"/>

<div class="section_1">
    <h2>Indirizzo di Spedizione</h2>
    <div id="shipping-addresses">
        <!--- aggiungo le informazioni con Ajax
<%--        <%
<%--            if (addresses != null && addresses.size() != 0) {
<%--                Iterator<?> it = addresses.iterator();
<%--                while (it.hasNext()) {
<%--                    AddressUs ad = (AddressUs) it.next();
<%--        %>
<%--        <div>
<%--            <input type="checkbox" name="selectedAddress" value="<%=ad.getNum_ID() %>">
<%--            <%=ad.toString()%>
<%--        </div>
<%--        <%      } //chiudo while
<%--        } //chiuso if
<%--        %>--%>-->
    </div>
    <button class="add-button" onclick="aggiungiIndirizzo()">Aggiungi Nuovo Indirizzo</button>
    <div id="new-address-form" class="hidden">
        <!--form per l'aggiunta di un nuovo indirizzo celato se l'utente non clicca il bottone-->
        <form id="addressForm" action="AddressManagement" method="post" onsubmit="return validateFormAd()">
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

            <input type="submit" value="Add Adress"><input type="reset" value="Reset">
        </form>
    </div>
</div>
<div class="section_2">
    <h2>Metodo di Pagamento</h2>
    <div id="shipping-payment">
        <!-- Aggiungo le info con ajax
<%--        <%--%>
<%--            if (payMethods != null && payMethods.size() != 0) {--%>
<%--                Iterator<?> it = payMethods.iterator();--%>
<%--                while (it.hasNext()) {--%>
<%--                    PayMethod pay = (PayMethod) it.next();--%>
        %>
<%--        <div>--%>
<%--            <input type="checkbox" name="selectedPayMethod" value="<%= pay.getNumId() %>">
<%--            <%= pay.toString()%>
<%--         </div>
<%--         <% //     } //chiudo while
<%--         //} //chiuso if
<%--         %>
<%--         --%>-->
    </div>
    <button class="add-button" onclick="aggiungiPayMethods()">Aggiungi Nuovo Metodo di pagamento</button>
    <div id="new-payMethod-form" class="hidden">
        <!--form per l'aggiunta di un nuovo metodo di pagamento celato se l'utente non clicca il bottone-->
        <form id="payMethodsForm" action="payMethodsManager" method="post" onsubmit="return validateFormPM()">
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
            <span id="titolareError" class="error-message">Il campo Titolare Carta è obbligatorio.</span><br>

            <input type="submit" value="Add Methods"><input type="reset" value="Reset">
        </form>
    </div>
</div>
<jsp:include page="Footer.jsp"/>
</body>
</html>
