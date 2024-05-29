
function validateFormPM() {
    var isValid = true;
    clearErrors();
    // Numero Carta deve essere fisso a 16, non di più non di meno
    var numeroCarta = document.getElementById("NumeroCarta").value;
    var numeroCartaPattern = /^[0-9]{16}$/;
    if (!numeroCartaPattern.test(numeroCarta)) {
        showError(document.getElementById("NumeroCarta"), "NumeroCartaError");
        isValid = false;
    }
    // Data Scadenza non l'ho reso data
    var meseScadenza = document.getElementById('meseScadenza').value;
    var annoScadenza = document.getElementById('annoScadenza').value;
    var dataScadenza = meseScadenza + annoScadenza;

    var dataScadenzaPattern = /^(0[1-9]|1[0-2])\d{2}$/;
    if (!dataScadenzaPattern.test(dataScadenza)) {
        showError(document.getElementById("meseScadenza"), "dataScadenzaError");
        showError(document.getElementById("annoScadenza"), "dataScadenzaError");
        isValid = false;
    }

    // CVV
    var cvv = document.getElementById('cvv').value;
    var cvvPattern = /^[0-9]{3}$/;
    if (!cvvPattern.test(cvv)) {
        showError(document.getElementById("cvv"), "CvvError");
        isValid = false;
    }
    // Circuito
    var circuito = document.getElementById('circuito').value;
    if (circuito.trim() === '') {
        showError(document.getElementById("circuito"), "circuitoError");
        isValid = false;
    }

    // Titolare Carta
    var titolareCarta = document.getElementById('titolareCarta').value;
    if (titolareCarta.trim() === '') {
        showError(document.getElementById("titolareCarta"), "titolareError");
        isValid = false;
    }

    return isValid;
}
    function showError(element, errorElementId) {
        element.classList.add("error");
        document.getElementById(errorElementId).style.display = "block";
    }

    function clearErrors() {
        var errors = document.querySelectorAll(".error");
        errors.forEach(function(element) {
            element.classList.remove("error");
        });

        var errorMessages = document.querySelectorAll(".error-message");
        errorMessages.forEach(function(element) {
            element.style.display = "none";
        });
    }



