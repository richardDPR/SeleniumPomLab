package com.demoblaze.steps;

import com.demoblaze.pages.CartPage;
import com.demoblaze.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartSteps {
    private CartPage cartPage;

    @Then("el producto {string} debe aparecer en el carrito")
    public void verificarProductoEnCarrito(String producto) {
        if (cartPage == null) {
            cartPage = new com.demoblaze.pages.CartPage(DriverFactory.getDriver());
        }
        cartPage.openCart();
        assertTrue(cartPage.isProductInCart(producto),
            "El producto '" + producto + "' no se encontró en el carrito");
    }

    @After
    public void cerrarNavegador() {
        DriverFactory.quitDriver();
    }
}
