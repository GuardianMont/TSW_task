
$(document).ready(function() {
    // Load existing addresses and payment methods on page load
    loadAddresses();
    loadPaymentMethods();

    $('#check-out-form').on("submit", function(event){
        let addressSelected = $("input[name='selectedAddress']:checked").val();
        let paymentSelected = $("input[name='selectedPayMethod']:checked").val();

        if (!addressSelected) {
            alert("Per proseguire selezionare un indirizzo");
            return false;
        }

        if (!paymentSelected) {
            alert("Per proseguire selezionare un metodo di pagamento");
            return false;
        }

        return true;
    });

    // Submit address form via AJAX
    $('#addressForm').on('submit', function(event) {
        event.preventDefault();
        if (validateFormAd()) {
            var formData = $(this).serialize();
            console.log("Sending data:", formData);
            $.ajax({
                contentType:"application/x-www-form-urlencoded; charset=UTF-8",
                url: "AddressManagement",
                type: 'POST',
                data: formData,
                success: function(response) {
                    if(response.success){
                        console.log("Operazione completata con successo")
                        $('#new-address-form').addClass('hidden');
                        loadAddresses();
                        resetAddressForm();
                    }else{
                        alert('Errore: ' + response.error);
                    }
                },
                error: function(xhr, status, error) {
                    console.error('Error adding address:', error);
                    alert('Errore durante l\'aggiunta dell\'indirizzo: ' + xhr.responseText);
                }
            });
        }
    });

    $('#payMethodsForm').on('submit', function(event) {
        event.preventDefault();
        if (validateFormPM()) {
            var formData = $(this).serialize();
            console.log("Sending data:", formData);
            $.ajax({
                contentType:"application/x-www-form-urlencoded; charset=utf-8",
                url: 'payMethodsManager',
                type: 'POST',
                data: formData,
                success: function(response) {
                    if (response && response.success){
                        console.log("Operazione completata con successo")
                        $('#new-payMethod-form').addClass('hidden');
                        loadPaymentMethods();
                        resetPaymentForm();
                    }else{
                        alert('Errore durante l\'aggiunta del metodo di pagamento: ' + response.error);
                    }
                },
                error: function(xhr, status, error) {
                    console.error('Error adding payment method:', error);
                }
            });
        }
    });


});

function loadAddresses() {
    $.ajax({
        url: 'AddressManagement',
        type: 'POST',
        data: {opzione: "show"},
        dataType: 'json',
        success: function(response) {
            $('#shipping-addresses').empty();
            response.forEach(function(AddressUs) {
                $('#shipping-addresses').append(
                    '<div><input type="radio" name="selectedAddress" value="' + AddressUs.numId + '">' + AddressUs.toString() + '</div>'
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error loading addresses:', error);
        }
    });
}

function loadPaymentMethods() {
    $.ajax({
        url: 'payMethodsManager',
        type: 'POST',
        data: {opzione: "show"},
        dataType: 'json',
        success: function(response) {
            $('#shipping-payment').empty();
            response.forEach(function(payMethod) {
                $('#shipping-payment').append(
                    '<div><input type="radio" name="selectedPayMethod" value="' + payMethod.numId + '"> Numero carta: ' + payMethod.numCarta +
                    '<br> Data scadenza: ' + payMethod.dataScadenza +
                    'Titolare Carta: ' + payMethod.titolareCarta + '</div>'
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error loading payment methods:', error);
        }
    });
}

function aggiungiIndirizzo() {
    document.getElementById("new-address-form").classList.remove("hidden");
    document.getElementById("remove-button").classList.remove("hidden");
    document.getElementById("add-button").classList.add("hidden");
}

function removeForm(){
    document.getElementById("new-address-form").classList.add("hidden");
    document.getElementById("remove-button").classList.add("hidden");
    document.getElementById("add-button").classList.remove("hidden");
}
function aggiungiPayMethods() {
    document.getElementById("new-payMethod-form").classList.remove("hidden");
    document.getElementById("remove-pay-form").classList.remove("hidden");
    document.getElementById("add-pay-button").classList.add("hidden");
}

function removePayForm(){
    document.getElementById("new-payMethod-form").classList.add("hidden");
    document.getElementById("remove-pay-form").classList.add("hidden");
    document.getElementById("add-pay-button").classList.remove("hidden");
}

function resetAddressForm() {
    $('#addressForm')[0].reset();
}

function resetPaymentForm() {
    $('#payMethodsForm')[0].reset();
}

function validateSelections(){
    let addressSelected = $("input[name='selectedAddress']:checked").val();
    let paymentSelected = $("input[name='selectedPayMethod']:checked").val();

    if (!addressSelected) {
        alert("Per proseguire selezionare un indirizzo");
        return false;
    }

    if (!paymentSelected) {
        alert("Per proseguire selezionare un metodo di pagamento");
        return false;
    }

    return true;
}