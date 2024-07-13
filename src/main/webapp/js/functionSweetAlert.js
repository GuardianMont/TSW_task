function confirmDelete(productId) {
    Swal.fire({
        title: 'Sei sicuro?',
        text: "Non potrai annullare questa azione!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sì, elimina!',
        cancelButtonText: 'Annulla'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = 'product?opzione=delete&id=' + productId;
        }
    });
}