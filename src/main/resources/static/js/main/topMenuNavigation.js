(function () {
    const header = document.querySelector('.header');
    const mobileMenu = document.querySelector('.mobile-menu');
    const icon = document.querySelector('.icon-container');
    icon.onclick = function () {
        header.classList.toggle('menu-open');
        mobileMenu.classList.toggle('menu-open');
    }
}());