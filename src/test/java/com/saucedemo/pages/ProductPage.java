package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Localizadores
    private By productTitle    = By.cssSelector("#product-form > h1");
    private By productPrice    = By.cssSelector("span.product-price");
    private By addToCartButton = By.id("add");
    // Contador del carrito que Shopify actualiza via AJAX tras agregar un producto
    private By cartCount       = By.cssSelector("#cart-target-desktop, .cart-target, .count");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public String getProductTitle() {
        WebElement titleElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(productTitle)
        );
        return titleElement.getText().trim();
    }

    public String getProductPrice() {
        WebElement priceElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(productPrice)
        );
        return priceElement.getText().trim();
    }

    public void clickAddToCart() {
        // Scroll al botón y clic via JS para evitar bloqueos de overlays
        WebElement button = wait.until(
            ExpectedConditions.visibilityOfElementLocated(addToCartButton)
        );
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'});", button
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

        // Esperar que el contador del carrito sea mayor a 0 (AJAX completado)
        wait.until(driver -> {
            try {
                String text = driver.findElement(cartCount).getText().trim();
                // El contador puede ser "(1)" o "1"
                String digits = text.replaceAll("[^0-9]", "");
                return !digits.isEmpty() && Integer.parseInt(digits) > 0;
            } catch (Exception e) {
                return false;
            }
        });
    }
}
