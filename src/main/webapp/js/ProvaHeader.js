document.addEventListener('DOMContentLoaded', function() {
    const menuToggle = document.querySelector('.menu-toggle');
    const header = document.querySelector('header');

    menuToggle.addEventListener('click', function() {
        header.classList.toggle('active'); // Aggiunge o rimuove la classe 'active' dall'header
        menuToggle.classList.toggle('active'); // Aggiunge o rimuove la classe 'active' al menu toggle
    });
});
