function validateForm() {
    var via = document.getElementById("via").value;
    var nCivico = document.getElementById("n_civico").value;
    var preferenze = document.getElementById("preferenze").value;
    var cap = document.getElementById("cap").value;
    var citta = document.getElementById("citta").value;
    var provincia = document.getElementById("provincia").value;

    // Regular expressions for validation
    var capPattern = /^[0-9]{5}$/;
    var provinciaPattern = /^[A-Za-z]{2}$/;

    if (via.trim() === "") {
        alert("Il campo 'Via' è obbligatorio.");
        return false;
    }

    if (nCivico.trim() === "" || isNaN(nCivico) || parseInt(nCivico) <= 0) {
        alert("Inserisci un numero civico valido.");
        return false;
    }

    if (cap.trim() === "" || !capPattern.test(cap)) {
        alert("Inserisci un CAP valido (5 cifre).");
        return false;
    }

    if (citta.trim() === "") {
        alert("Il campo 'Città' è obbligatorio.");
        return false;
    }

    if (provincia.trim() === "" || !provinciaPattern.test(provincia)) {
        alert("Inserisci una provincia valida (2 lettere).");
        return false;
    }

    return true;  // Se tutti i campi sono validi
}