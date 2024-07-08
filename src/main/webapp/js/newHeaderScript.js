document.addEventListener("DOMContentLoaded", function() {

    //carico dinamicamente l'svg del logo
    fetch('uploadFile/logo.svg')
        .then(response => response.text())
        .then(data => {
            document.getElementById('logo-container').innerHTML = data;
            let svg = document.querySelector('#logo-container svg');
            svg.classList.add('logo')

            window.addEventListener('resize', doScrollAnimations);
            window.addEventListener('scroll', doScrollAnimations);
            doScrollAnimations()
        })
        .catch(error => console.error('Error loading SVG:', error));
});

function doScrollAnimations() {

    doHeaderAnimations();

    doTextAnimations();
}

function doHeaderAnimations(){
    const header = document.getElementById('header');
    const logo = document.querySelector('.logo');
    const links = document.getElementById('link-container');
    const icons = document.getElementById('icons');
    const conteggioCarrello = document.getElementById('numeroProdotti');

    if (window.scrollY > 0) {
        header.classList.remove('transparent');
        header.classList.add('colored');
        logo.classList.add('logo-colored');
        links.classList.add('link-colored');
        icons.classList.add('icon-colored');
        if(conteggioCarrello !== null)
            conteggioCarrello.classList.add('numeroProdotti-colored');


    } else {
        header.classList.remove('colored');
        header.classList.add('transparent');
        logo.classList.remove('logo-colored');
        links.classList.remove('link-colored');
        icons.classList.remove('icon-colored');
        if(conteggioCarrello !== null)
            conteggioCarrello.classList.remove('numeroProdotti-colored');
    }
}


function doTextAnimations() {
    const firstImageContainer = document.getElementById('fist-image-container');
    const firstText = document.getElementById('text-over-image');
    const imageRect = firstImageContainer.getBoundingClientRect();
    const viewWindowBottom = window.innerHeight + window.scrollY;
    const imageBottom = imageRect.height;

    if (viewWindowBottom < imageBottom) {
        firstText.classList.add('fixed');
        firstText.style.top = `${(imageBottom)*0.85 - (imageBottom-window.innerHeight)}px`;
    } else if (viewWindowBottom >= imageBottom) {
        firstText.style.top = `${(imageBottom)*0.85}px`;
        firstText.classList.remove('fixed');
        firstText.classList.remove('text-to-animate');
        firstText.classList.remove('transparent');
    }
}
