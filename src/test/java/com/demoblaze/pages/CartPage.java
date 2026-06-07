package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By cartItems = By.cssSelector("#tbodyid tr td:nth-child(2)");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openCart() {
        driver.findElement(By.id("cartur")).click();
        // Esperar que el contenedor del carrito esté presente (aunque esté vacío)
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbodyid")));
    }

    public boolean isProductInCart(String productName) {
        // esperar hasta que aparezcan filas en el carrito (si las hay)
        int attempts = 0;
        List<WebElement> items = driver.findElements(cartItems);
        while (items.isEmpty() && attempts < 10) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            items = driver.findElements(cartItems);
            attempts++;
        }
        List<String> names = items.stream().map(WebElement::getText).map(String::trim).map(String::toLowerCase).collect(Collectors.toList());
        String expected = productName.trim().toLowerCase();
        return names.stream().anyMatch(n -> n.contains(expected));
    }

    public boolean isCartEmpty() {
        int attempts = 0;
        List<WebElement> items = driver.findElements(cartItems);
        while (items.isEmpty() && attempts < 10) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            items = driver.findElements(cartItems);
            attempts++;
        }
        return items.isEmpty();
    }
}
