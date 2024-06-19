function checkValidityQuantity(input) {
    input.value = input.value.replace(/\D/g, '');
    var value = parseInt(input.value);
    if (value < parseInt(input.min)) {
        input.value = input.min; // Imposta il valore al minimo ovvero 1
    }
    if (value > parseInt(input.max)) {
        input.value = input.max; // Imposta il valore al massimo (in base al prodotto)
    }
}
function incrementQuantity(button) {
    var input = button.previousElementSibling;
    var max = parseInt(input.max);
    var currentValue = parseInt(input.value);
    if (currentValue < max) {
        input.value = currentValue + 1;
        updateQuantity(input.form);
        updateTotalPrice();
    }
}

function decrementQuantity(button) {
    var input = button.nextElementSibling;
    var min = parseInt(input.min);
    var currentValue = parseInt(input.value);
    if (currentValue > min) {
        input.value = currentValue - 1;
        updateQuantity(input.form);
        updateTotalPrice();
    }
}

function updateQuantity(form) {
    clearErrors();
    const quantita = form.querySelector('input[name="quantity"]');
    if (quantita && quantita.value){
        const quantity = parseInt(quantita.value, 10);
        if (!isNaN(quantity) && quantity > 0) {
            form.submit();
        } else {
            document.getElementById("quantity-error").textContent="la quantità è necessaria non può essere nulla"
            document.getElementById("quantity-error").style.display="block";
        }
    }else{
        document.getElementById("quantity-error").textContent="la quantità è necessaria non può essere nulla"
        document.getElementById("quantity-error").style.display="block";
    }
}

function updateTotalPrice() {
    // Calcola e aggiorna la spesa totale
    var totalPrice = 0;
    var quantityInputs = document.querySelectorAll('input[name="quantity"]');
    quantityInputs.forEach(function(input) {
        var itemPrice = parseFloat(input.parentNode.previousElementSibling.textContent);
        var quantity = parseInt(input.value);
        totalPrice += itemPrice * quantity;
    });
    document.querySelector('.spesa_totale').textContent = "Spesa totale: €" + totalPrice.toFixed(2);
}

document.addEventListener('DOMContentLoaded', function() {
    // Aggiungi gestori di eventi per le modifiche della quantità
    var quantityInputs = document.querySelectorAll('input[name="quantity"]');
    quantityInputs.forEach(function(input) {
        input.addEventListener('blur', function() {
            checkValidityQuantity(input);
            updateQuantity(input.form);
            updateTotalPrice();
        });

        input.addEventListener('change', function() {
            checkValidityQuantity(input);
            updateQuantity(input.form);
            updateTotalPrice();
        });
    });
    updateTotalPrice();
});