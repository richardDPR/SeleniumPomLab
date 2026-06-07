package com.demoblaze.steps;

import com.demoblaze.pages.HomePage;
import com.demoblaze.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NavigationSteps {
    private HomePage homePage;

    @When("selecciono la categoría {string}")
    public void seleccionoCategoria(String categoria) {
        if (homePage == null) {
            homePage = new HomePage(DriverFactory.getDriver());
        }
        homePage.selectCategory(categoria);
    }

    @Then("debo ver productos relacionados con {string}")
    public void verificarProductosCategoria(String categoria) {
        if (homePage == null) {
            homePage = new HomePage(DriverFactory.getDriver());
        }
        var productos = homePage.getProductTitles();
        assertTrue(!productos.isEmpty(), "No se encontraron productos en la categoría " + categoria);
    }

    @After
    public void cerrarNavegador() {
        DriverFactory.quitDriver();
    }
}
