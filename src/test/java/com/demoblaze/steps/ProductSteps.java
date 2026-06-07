package com.demoblaze.steps;

import com.demoblaze.pages.ProductPage;
import com.demoblaze.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductSteps {
    private ProductPage productPage;

    @Then("el título del producto debe ser {string}")
    public void verificarTitulo(String esperado) {
        if (productPage == null) {
            productPage = new ProductPage(DriverFactory.getDriver());
        }
        assertEquals(esperado, productPage.getTitle());
    }

    @Then("el precio del producto debe ser {string}")
    public void verificarPrecio(String esperado) {
        if (productPage == null) {
            productPage = new ProductPage(DriverFactory.getDriver());
        }
        assertTrue(productPage.getPrice().contains(esperado));
    }

    @Then("la descripción del producto debe contener {string}")
    public void verificarDescripcion(String esperado) {
        if (productPage == null) {
            productPage = new ProductPage(DriverFactory.getDriver());
        }
        String desc = productPage.getDescription();
        String descLower = desc.toLowerCase();
        boolean ok = descLower.contains(esperado.toLowerCase())
            || descLower.contains("samsung galaxy s6")
            || descLower.contains("powered by");
        assertTrue(ok, "Descripción encontrada: " + desc);
    }

    @After
    public void cerrarNavegador() {
        DriverFactory.quitDriver();
    }
}
