/* **************************************** all pages **************************************** */
let skipLink = document.querySelector('.skip-link');
skipLink.addEventListener('click', function (e) {
    e.preventDefault();
    document.querySelector(skipLink.getAttribute('href')).focus();
});

/* **************************************** product.html **************************************** */
$(document).ready(function(){
    // Tooltip
    $('[data-toggle="tooltip"]').tooltip();
})
function normalizeN(n) {
    let slideCount = document.getElementsByClassName("slide-for-js").length;
    if (n >= slideCount) {
        n = 0;
    }
    if (n < 0) {
        n = slideCount - 1;
    }
    return n;
}
function setSlideDisplay(newIndex, active) {
    let newSlide = document.getElementById(newIndex);
    let activeSlide = document.getElementById(active);
    activeSlide.className = activeSlide.className.replace("d-block", "d-none");
    newSlide.className = newSlide.className.replace("d-none", "d-block");
}
/* ************************ Product LightBox ************************ */
let controlsIndex = 0;
let activeIndex = 0;
// Next/previous controls
function productControls(n) {
    controlsIndex = controlsIndex + parseInt(n);
    showProductSlides(controlsIndex);
}
// Thumbnail image/indicator controls
function currentProductSlide(n) {
    controlsIndex = parseInt(n);
    showProductSlides(controlsIndex);
}
function showProductSlides(n) {
    n = normalizeN(n);
    controlsIndex = n;
    setSlideDisplay(n, activeIndex);

    let activeIndicator;
    let newIndicator;
    let md = window.matchMedia("(min-width: 768px)");
    if (md.matches) {
        activeIndicator = document.getElementById('thumb-' + activeIndex);
        newIndicator = document.getElementById('thumb-' + n);
    } else {
        activeIndicator = document.getElementById('indicator-' + activeIndex);
        newIndicator = document.getElementById('indicator-' + n);
    }
    activeIndicator.className = activeIndicator.className.replace("active ", "");
    newIndicator.className = newIndicator.className.replace("", "active ");
    activeIndex = n;
}

/* ************************ Product Modal ************************ */
let modalControl;
let modalActive;
$('#lightboxModal').on('show.bs.modal', function (e) {
    modalControl = activeIndex;
    modalActive = activeIndex;

    currentLightboxSlide(modalControl);
})
$('#lightboxModal').on('hide.bs.modal', function (e) {
    let slides = document.getElementsByClassName("lightbox-slides");
    slides[modalActive].style.display = "none";

    let thumbs = document.getElementsByClassName("for-js");
    thumbs[modalActive].className = thumbs[modalActive].className.replace("active ", "");
})
// Next/previous controls
function plusLightboxSlides(n) {
    modalControl = modalControl + parseInt(n);
    showLightboxSlides(modalControl);
}
// Thumbnail image/indicator controls
function currentLightboxSlide(n) {
    modalControl = parseInt(n);
    showLightboxSlides(modalControl);
}
function showLightboxSlides(n) {
    n = normalizeN(n);
    modalControl = n;
    let slides = document.getElementsByClassName("lightbox-slides");
    slides[modalActive].style.display = "none";
    slides[modalControl].style.display = "block";

    let md = window.matchMedia("(min-width: 768px)");
    let indicators;
    if (md.matches) {
        indicators = document.getElementsByClassName("for-js");
    } else {
        indicators = document.querySelector("#lightboxModal carousel-indicators")[0].children;
    }
    indicators[modalActive].className = indicators[modalActive].className.replace("active ", "");

    indicators[modalControl].className = "active " + indicators[modalControl].className;
    modalActive = modalControl;
}

function addItem2(id) {
    $.post("../../addItem/" + id);
    // use on localhost when runnning with tomcat
    // $.post("../../escotech/addItem/" + id);
}


/* **************************************** cart.html **************************************** */
function updateCart() {
    const cartProducts = document.querySelectorAll(".product-card");
    let arr = [];
    cartProducts.forEach(function(data) {
        let inputElement = data.querySelector("input");
        const CartProduct = {
            productId: inputElement.getAttribute("data-quant"),
            quantity: inputElement.getAttribute("value")
        };
        arr.push(CartProduct);
    });
    $.ajax({
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        url: 'updateCart/',
        data: JSON.stringify(arr)
    })
}
function updateQuantity(id) {
    let element = document.getElementById(id).querySelector("input");
    let quantity = element.getAttribute("value");
    let newQuantity = element.value;
    newQuantity = parseInt(newQuantity);
    let inventory = element.getAttribute("max");
    inventory = parseInt(inventory);
    if (newQuantity > inventory) {
        newQuantity = inventory;
    }
    if (newQuantity < 0) {
        newQuantity = 0;
    }
    element.setAttribute("value", newQuantity.toString());
    element.value = newQuantity.toString();
    updateCartSummary(id, newQuantity - quantity);
    updateCart();
}
function increment(id) {
    let container = document.getElementById(id);
    let element = container.querySelector("div input");
    let quantity = element.getAttribute("value");
    quantity = parseInt(quantity);
    let inventory = element.getAttribute("max");
    inventory = parseInt(inventory);
    if (quantity < inventory) {
        element.setAttribute("value", (quantity + 1).toString());
        element.value = (quantity + 1).toString();
        updateCartSummary(id, 1);
        updateCart();
    }
}
function toUSD (number) {
    let formatter = new Intl.NumberFormat('en-us', {
        style: 'currency',
        currency: 'USD'
    });
    return formatter.format(number);
}
function decrement(id) {
    let container = document.getElementById(id);
    let element = container.querySelector("div input");
    let quantity = element.getAttribute("value");
    quantity = parseInt(quantity);
    if (quantity > 0) {
        element.setAttribute("value", (quantity - 1).toString());
        element.value = (quantity - 1).toString();
        updateCartSummary(id, -1);
        updateCart();
    }
}
function updateCartSummary(id, quantityDelta) {
    let cartQuantityElement = document.getElementById("totalQuantity");
    let totalQuantity = parseInt(cartQuantityElement.innerHTML);
    totalQuantity += quantityDelta;
    cartQuantityElement.innerHTML = totalQuantity.toString();

    let costDelta = document.getElementById('unit-cost-' + id);
    costDelta = currencyToFloat(costDelta);
    costDelta = costDelta * quantityDelta;

    let subtotalElement = document.getElementById("subtotal");
    let subtotal = currencyToFloat(subtotalElement);
    subtotal += costDelta;
    subtotalElement.innerHTML = toUSD(subtotal).toString();
}
function currencyToFloat(currency) {
    let number = currency.innerText.replace(/,/g, '');
    number = number.replace("$", '');
    return parseFloat(number);
}

/* **************************************** address.html **************************************** */
function updateStateEnum(country) {
    $.get("state/enum/" + country).done(function (fragment) {
        $("#stateUpdate").replaceWith(fragment);
    });
}
/* **************************************** products.html **************************************** */
function addItem(id) {
    // use on localhost when runnning without tomcat
    $.post("addItem/" + id);

    // use on localhost when runnning with tomcat
    // $.post("escotech/addItem/" + id);
}