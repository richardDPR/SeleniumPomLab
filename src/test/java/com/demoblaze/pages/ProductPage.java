package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Localizadores
    private By productTitle = By.cssSelector(".name");
    private By productPrice = By.cssSelector(".price-container");
    private By productDescription = By.cssSelector("#more-information .description");
    private By addToCartButton = By.linkText("Add to cart");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productTitle))
                   .getText().trim();
    }

    public String getPrice() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productPrice))
                   .getText().trim();
    }

    public String getDescription() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(productDescription))
                       .getText().trim();
        } catch (Exception e) {
            // fallback: intentar obtener el contenedor principal si la subclase no existe
            By altDesc = By.cssSelector("#more-information");
            return wait.until(ExpectedConditions.visibilityOfElementLocated(altDesc)).getText().trim();
        }
    }

    public void addToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        // Demoblaze muestra un alert al agregar al carrito
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }
}
