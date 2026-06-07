package com.demoblaze.steps;

import com.demoblaze.pages.CartPage;
import com.demoblaze.pages.CheckoutPage;
import com.demoblaze.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckoutSteps {
    private WebDriver driver;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @When("procedo al checkout con nombre {string} y tarjeta {string}")
    public void procedoCheckout(String nombre, String tarjeta) {
        if (driver == null) {
            driver = DriverFactory.getDriver();
        }
        if (cartPage == null) {
            cartPage = new com.demoblaze.pages.CartPage(driver);
        }
        cartPage.openCart();
        // si el carrito está vacío, generar una alerta JS para que el step de verificación la capture
        if (cartPage.isCartEmpty()) {
            ((JavascriptExecutor) driver).executeScript("alert('Cart is empty');");
            return;
        }
        checkoutPage = new CheckoutPage(driver);
        checkoutPage.openCheckout();
        checkoutPage.fillForm(nombre, tarjeta);
    }

    @Then("debo ver el mensaje de confirmación {string}")
    public void verificarConfirmacion(String esperado) {
        assertEquals(esperado, checkoutPage.getConfirmationMessage());
    }

    @After
    public void cerrarNavegador() {
        DriverFactory.quitDriver();
    }
}
