package com.demoblaze.steps;

import com.demoblaze.utils.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import com.demoblaze.pages.CatalogPage;
import com.demoblaze.pages.ProductPage;
import com.demoblaze.pages.CartPage;

import org.openqa.selenium.WebDriver;

public class CommonSteps {
    private WebDriver driver;
    private CatalogPage catalogPage;
    private ProductPage productPage;
    private CartPage cartPage;
    
    @Given("que estoy en la página principal de Demoblaze")
    public void abrirPaginaPrincipal() {
        driver = DriverFactory.getDriver();
        catalogPage = new CatalogPage(driver);   // inicialización
        catalogPage.openHome();                  // abrir página principal
    }

    @When("selecciono el producto {string}")
    public void seleccionoProducto(String producto) {
        catalogPage.selectProduct(producto);
        productPage = new ProductPage(driver); // inicialización manual
    }


    @When("agrego el producto al carrito")
    public void agregarProductoAlCarrito() {
        productPage.addToCart();
        cartPage = new CartPage(driver); // inicialización del carrito
    }

}
