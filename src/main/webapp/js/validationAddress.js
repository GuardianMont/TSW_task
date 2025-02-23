
    function validateFormAd() {
        var isValid = true;

        var via = document.getElementById("via").value;
        var nCivico = document.getElementById("n_civico").value;
        var preferenze = document.getElementById("preferenze").value;
        var cap = document.getElementById("cap").value;
        var citta = document.getElementById("citta").value;
        var provincia = document.getElementById("provincia").value;

        // Regular expressions for validation
        var stringaViaPatter = /^[a-zA-Z0-9\s]+$/
        var stringPatter = /^[a-zA-Z\s]+$/
        var capPattern = /^[0-9]{5}$/;
        var provinciaPattern = /^[A-Za-z]{2}$/;
        var civicoPattern =/^[0-9]{1,4}$/
        clearErrors(); // per la cancellazione di errori
        //se prima ce ne erano altri

        if (via.trim() === "") {
            showError(document.getElementById("via"), "via-error", "il campo via è obbligatori");
            console.log("Errore nella validazione: via");
            isValid = false;
        }
        if (!stringaViaPatter.test(via)){
            showError(document.getElementById("via"), "via-error", "il campo via è un alfanumerico");
            console.log("Errore nella validazione: via");
            isValid = false;
        }

        if (nCivico.trim() === "" || isNaN(nCivico) || parseInt(nCivico) <= 0 || !civicoPattern.test(nCivico)) {
            showError(document.getElementById("n_civico"), "n_civico-error");
            console.log("Errore nella validazione: numero civico");
            isValid = false;
        }

        if (cap.trim() === "" || !capPattern.test(cap)) {
            showError(document.getElementById("cap"), "cap-error");
            isValid = false;
        }

        if (citta.trim() === "") {
            showError(document.getElementById("citta"), "citta-error");
            isValid = false;
        }
        if (!stringPatter.test(citta)){
            showError(document.getElementById("citta"), "citta-error", "città può contenere solo lettere");
            isValid = false;
        }

        if (provincia.trim() === "" || !provinciaPattern.test(provincia)) {
            showError(document.getElementById("provincia"), "provincia-error");
            isValid = false;
        }
        if (isValid) {
            console.log("Form valido, invio della richiesta AJAX...");
        }
        return isValid;  // Se tutti i campi sono validi
    }

    function showError(element, errorElementId, meggase) {
        element.classList.add("error");
        let error = document.getElementById(errorElementId);
        error.style.display = "block"
        error.innerText=meggase;
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
