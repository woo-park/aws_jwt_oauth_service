(function () {
    const header = document.querySelector('.header');
    const mobileMenu = document.querySelector('.mobile-menu');
    const icon = document.querySelector('.icon-container');
    const desktopMenu = document.querySelector('.desktop-menu');
    icon.onclick = function () {
        header.classList.toggle('menu-open');
        mobileMenu.classList.toggle('menu-open');
        desktopMenu.classList.toggle('menu-open');
    }
}());
