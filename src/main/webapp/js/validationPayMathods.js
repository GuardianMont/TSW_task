function validateFormPM() {
    clearErrors();  // Rimuovi gli errori esistenti prima di eseguire la validazione
    var isValid = true;

    // Numero Carta deve essere fisso a 16, non di più non di meno
    var numeroCarta = document.getElementById("NumeroCarta").value;
    var numeroCartaPattern = /^[0-9]{16}$/;

    if (!numeroCartaPattern.test(numeroCarta)) {
        console.log("Errore nella validazione: numero carta");
        showError(document.getElementById("NumeroCarta"), "NumeroCartaError");
        isValid = false;
    }

    // Data Scadenza
    var meseScadenza = document.getElementById('meseScadenza').value;
    var annoScadenza = document.getElementById('annoScadenza').value;

    if (!/^(0[1-9]|1[0-2])$/.test(meseScadenza)) {
        console.log("Errore nella validazione: mese");
        showError(document.getElementById("meseScadenza"), "meseScadenzaError");
        isValid = false;
    }

    if (!/^\d{2}$/.test(annoScadenza) || parseInt(annoScadenza) < (new Date().getFullYear() % 100)) {
        console.log("Errore nella validazione: anno");
        showError(document.getElementById("annoScadenza"), "annoScadenzaError");
        isValid = false;
    }

    var dataScadenza = meseScadenza + annoScadenza;
    var dataScadenzaPattern = /^(0[1-9]|1[0-2])\d{2}$/;
    if (!dataScadenzaPattern.test(dataScadenza)) {
        console.log("Errore nella validazione: data");
        showError(document.getElementById("meseScadenza"), "dataScadenzaError");
        showError(document.getElementById("annoScadenza"), "dataScadenzaError");
        isValid = false;
    }

    // CVV
    var cvv = document.getElementById('cvv').value;
    var cvvPattern = /^[0-9]{3,4}$/;  // Consenti anche 4 cifre per American Express
    if (!cvvPattern.test(cvv)) {
        console.log("Errore nella validazione: cvv");
        showError(document.getElementById("cvv"), "CvvError");
        isValid = false;
    }

    // Circuito
    var circuito = document.getElementById('circuito').value;
    if (circuito.trim() === '') {
        console.log("Errore nella validazione: circuito");
        showError(document.getElementById("circuito"), "circuitoError");
        isValid = false;
    }

    // Titolare Carta
    var titolareCarta = document.getElementById('titolareCarta').value;
    if (titolareCarta.trim() === '') {
        console.log("Errore nella validazione: titolare carta");
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
