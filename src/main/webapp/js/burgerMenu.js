document.addEventListener('DOMContentLoaded', function () {
    const burgerMenu = document.querySelector('.burger-menu');
    const burgerMenuContainer = document.querySelector('.burger-menu-container');

    burgerMenu.addEventListener('click', function () {
        burgerMenuContainer.classList.toggle('show');
    });
});