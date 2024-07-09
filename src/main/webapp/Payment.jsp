<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Indirizzo di Spedizione</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="css/notifica.css">
    <link rel="stylesheet" href="css/Payment.css">
    <link rel="stylesheet" href="css/confermaAcquisto.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="js/validationAddress.js"></script>
    <script src="js/validationPayMathods.js"></script>
    <script src="js/notifica.js"></script>
    <script src="js/loadAddressPay.js"></script>
</head>
<body>
<jsp:include page="Header.jsp"/>
<div class="main-container">
    <div class="container">
        <div class="section_1">
            <h2>Indirizzo di Spedizione</h2>
            <div id="shipping-addresses">
                <!--immetto le informazioni con ajax -->
            </div>
            <div id="add-button" class="button-container">
                <button class="add-button add-methods-button" onclick="aggiungiIndirizzo()">
                    <i class="fas fa-plus"></i> Aggiungi Nuovo Indirizzo
                </button>
            </div>
            <div id="remove-button" class="button-container hidden">
                <button class="remove-button" onclick="removeForm()">X</button>
            </div>
            <div id="new-address-form" class="hidden">
                <!--form per l'aggiunta di un nuovo indirizzo celato se l'utente non clicca il bottone-->
                <form id="addressForm" action="AddressManagement" method="POST">
                    <input type="hidden" name="opzione" value="add">
                    <label for="via">Via:</label>
                    <input id="via" name="via" type="text" maxlength="20" placeholder="Via Muni..." pattern="^[a-zA-Z0-9\s]+$" required>
                    <span id="via-error" class="error-message">Il campo 'Via' è obbligatorio.</span>

                    <label for="n_civico">Numero civico:</label>
                    <input id="n_civico" name="n_civico" type="text" maxlength="4" required>
                    <span id="n_civico-error" class="error-message">Inserisci un numero civico valido.</span>

                    <label for="preferenze">Preferenze spedizione:</label>
                    <textarea class="textarea" id="preferenze" name="preferenze" maxlength="30" placeholder="Inserire preferenze di spedizione" pattern="^[a-zA-Z0-9\s]+$"></textarea>

                    <label for="cap">CAP:</label>
                    <input id="cap" name="cap" type="text" maxlength="5" placeholder="00000" required>
                    <span id="cap-error" class="error-message">Inserisci un CAP valido (5 cifre).</span>

                    <label for="citta">Città:</label>
                    <input id="citta" name="citta" type="text" placeholder="Città" pattern="^[a-zA-Z0-9\s]+$" required>
                    <span id="citta-error" class="error-message">Il campo 'Città' è obbligatorio.</span>

                    <label for="provincia">Provincia:</label>
                    <input id="provincia" name="provincia" type="text" maxlength="2" required>
                    <span id="provincia-error" class="error-message">Inserisci una provincia valida (2 lettere).</span>

                    <div class="button-container">
                        <input type="submit" value="Aggiungi Indirizzo" class="add-address-button">
                        <input type="reset" value="Reset">
                    </div>
                </form>
            </div>
        </div>
        <div class="section_2">
            <h2>Metodo di Pagamento</h2>
            <div id="shipping-payment">
                <!--immetto le informazioni con ajax -->
            </div>
            <div id="add-pay-button" class="button-container">
                <button class="add-button add-methods-button" onclick="aggiungiPayMethods()">
                    <i class="fas fa-plus"></i> Aggiungi Nuovo Metodo di Pagamento
                </button>
            </div>
            <div id="remove-pay-form" class="button-container hidden">
                <button class="remove-button" onclick="removePayForm()">X</button>
            </div>
            <div id="new-payMethod-form" class="hidden">
                <!--form per l'aggiunta di un nuovo metodo di pagamento celato se l'utente non clicca il bottone-->
                <form id="payMethodsForm" action="payMethodsManager" method="post">
                    <input type="hidden" name="opzione" value="add">

                    <label for="NumeroCarta">Numero Carta:</label>
                    <input id="NumeroCarta" name="NumeroCarta" type="text" pattern="\d{16}" maxlength="16" required placeholder="Numero Carta">
                    <span id="NumeroCartaError" class="error-message">Il campo Numero Carta è obbligatorio.</span>

                    <div class="data-scadenza">
                        <label for="meseScadenza">Data Scadenza:</label>
                        <input id="meseScadenza" name="meseScadenza" type="text" maxlength="2" placeholder="MM" pattern="(0[1-9]|1[0-2])" required>
                        <label for="annoScadenza">/</label>
                        <input id="annoScadenza" name="annoScadenza" type="text" maxlength="2" placeholder="YY" pattern="([2-9][4-9]|[3-9][0-9])" required>
                    </div>
                    <span id="dataScadenzaError" class="error-message">Inserisci una data di scadenza valida (MM/YY).</span>

                    <label for="cvv">CVV:</label>
                    <input id="cvv" name="cvv" type="text" maxlength="3" placeholder="000" required>
                    <span id="CvvError" class="error-message">Inserisci un CVV valido (3 cifre).</span>

                    <label for="circuito">Circuito:</label>
                    <select id="circuito" name="circuito" required>
                        <option value="" disabled selected>Seleziona un circuito</option>
                        <option value="visa">Visa</option>
                        <option value="mastercard">Mastercard</option>
                        <option value="amex">American Express</option>
                        <option value="discover">Discover</option>
                        <option value="diners">Diners Club</option>
                        <option value="bancomat">Bancomat</option>
                    </select>
                    <span id="circuitoError" class="error-message">Il campo 'Circuito' è obbligatorio.</span>

                    <label for="titolareCarta">Titolare Carta:</label>
                    <input id="titolareCarta" name="titolareCarta" type="text" placeholder="Nome Cognome" pattern="^[a-zA-Z0-9\s]+$" required>
                    <span id="titolareError" class="error-message">Il campo Titolare Carta è obbligatorio.</span>

                    <div class="button-container">
                        <input type="submit" value="Aggiungi Metodo" class="add-methods-button">
                        <input type="reset" value="Reset">
                    </div>
                </form>
            </div>
        </div>
        <form id="check-out-form" action="CheckoutServlet" method="post">
            <input type="submit" value="Procedi al Checkout" class="proceed-button">
        </form>
    </div>
</div>
<jsp:include page="Footer.jsp"/>
</body>
</html>
