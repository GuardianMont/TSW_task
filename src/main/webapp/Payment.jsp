<html>
<head>
    <title>Indirizzo di Spedizione</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="js/validationAddress.js"></script>
    <script src="js/validationPayMathods.js"></script>
    <script src="js/notifica.js"></script>
    <script src="js/loadAddressPay.js"></script>

    <link rel="stylesheet" href="css/notifica.css">
    <link rel="stylesheet" href="css/Payment.css">
    <link rel="stylesheet" href="css/confermaAcquisto.css">
</head>
<body>
<jsp:include page="Header.jsp"/>
<div class="main-container">
    <div class="section_1">
        <h2>Indirizzo di Spedizione</h2>
        <div id="shipping-addresses">
            <!--immetto le informazioni con ajax -->
        </div>
        <div id="add-button" class="">
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
                <input id="via" name="via" type="text" maxlength="20" title="la via deve essere un alfanumerico"
                       placeholder="Via Muni..." pattern="^[a-zA-Z0-9\s]+$" autofocus required><br>
                <span id="via-error" class="error-message">Il campo 'Via' è obbligatorio.</span><br>

                <label for="n_civico">Numero civico </label><br>
                <input id="n_civico" name="n_civico" type="text" maxlength="4"
                       title="cvv è una stringa numeri di 4 valori" required><br>
                <span id="n_civico-error" class="error-message">Inserisci un numero civico valido.</span><br>

                <label for="preferenze">Preferenze spedizione:</label><br>
                <textarea class="textarea" id="preferenze" name="preferenze" title="il campo preferenza accetta solo valori alfanumerici"
                          maxlength="30" placeholder="inserire preferenze di spedizione" pattern="^[a-zA-Z0-9\s]+$"></textarea><br>

                <label for="cap">Cap:</label><br>
                <input id="cap" name="cap" type="text" maxlength="5"
                       placeholder="00000" required><br>
                <span id="cap-error" class="error-message">Inserisci un CAP valido (5 cifre).</span><br>

                <label for="citta">Città</label><br>
                <input id="citta" name="citta" type="text" title="la città deve essere un alfanumerico"
                       placeholder="città" pattern="^[a-zA-Z0-9\s]+$"><br>
                <span id="citta-error" class="error-message">Il campo 'Città' è obbligatorio.</span><br>

                <label for="provincia">Provincia</label><br>
                <input id="provincia" name="provincia" title="la provincia una stringa di lunghezza 2"
                       type="text" maxlength="2" required><br>
                <span id="provincia-error" class="error-message">Inserisci una provincia valida (2 lettere).</span><br>

                <input type="submit" value="Add Address" class="add-address-button"><input type="reset" value="Reset">
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
        <div id="remove-pay-form" class="hidden">
            <button class="remove-button" onclick="removePayForm()">X</button>
        </div>
        <div id="new-payMethod-form" class="hidden">
            <!--form per l'aggiunta di un nuovo metodo di pagamento celato se l'utente non clicca il bottone-->
            <form id="payMethodsForm" action="payMethodsManager" method="post">
                <input type="hidden" name="opzione" value="add">

                <label for="NumeroCarta">Numero Carta:</label><br>
                <input id="NumeroCarta" name="NumeroCarta" type="text" title ="il numero di carta è di 16 numeri"
                       pattern="\d{16}" maxlength="16" required placeholder="numero carta"><br>
                <span id="NumeroCartaError" class="error-message">Il campo Numero Carta è obbligatorio.</span><br>

                <div class="data-scadenza">
                      <label for="meseScadenza">Data scadenza:</label><br>
                      <input id="meseScadenza" name="meseScadenza" type="text" maxlength="2" placeholder="MM"
                       pattern="(0[1-9]|1[0-2])" title="inserire un mese valido" required>
                      <span id="meseScadenzaError" class="error-message">Il mese deve essere compreso tra 01 e 12.</span><br>

                      <label for="annoScadenza">/</label><br>
                      <input id="annoScadenza" name="annoScadenza" type="text" maxlength="2" title="inserire un anno valido"
                       placeholder="YY" pattern="([2-9][4-9]|[3-9][0-9])" required><br>
                      <span id="dataScadenzaError" class="error-message">Inserisci una Data Scadenza valida (MMYY).</span><br>
                      <span id="annoScadenzaError" class="error-message">l'anno deve essere un valore valido</span><br>
                </div>

                <label for="cvv">Cvv:</label><br>
                <input id="cvv" name="cvv" type="text" maxlength="3" placeholder="000"
                        title="inserire un cvv valido" required>
                <span id="CvvError" class="error-message">Inserisci un CVV valido (3 cifre).</span><br>

                <label for="circuito">Circuito:</label><br>
                <select id="circuito" name="circuito" required>
                    <option value="" disabled selected>Seleziona un circuito</option>
                    <option value="visa">Visa</option>
                    <option value="mastercard">Mastercard</option>
                    <option value="amex">American Express</option>
                    <option value="discover">Discover</option>
                    <option value="diners">Diners Club</option>
                    <option value="bancomat">Bancomat</option>
                </select><br>
                <span id="circuitoError" class="error-message">Il campo 'Circuito' è obbligatorio.</span><br>

                <label for="titolareCarta">Titolare Carta:</label><br>
                <input id="titolareCarta" name="titolareCarta" type="text"
                       placeholder="nome cognome" pattern="^[a-zA-Z0-9\s]+$" required><br>
                <span id="titolareError" class="error-message">Il campo Titolare Carta è obbligatoria.</span><br>

                <input type="submit" value="Add Methods" class="add-methods-button"><input type="reset" value="Reset">
            </form>
        </div>
    </div>

    <form id="check-out-form" action="CheckoutServlet" method="post">
        <input type="submit" value="Procedi al checkOut" class="proceed-button">
    </form>
</div>
<jsp:include page="Footer.jsp"/>
</body>
</html>
