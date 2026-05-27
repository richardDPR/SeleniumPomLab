package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CartPage {

    private static final String CART_URL = "https://sauce-demo.myshopify.com/cart";

    private WebDriver driver;
    private WebDriverWait wait;

    // Localizadores — página /cart (misma estructura que el drawer)
    private By cartItems = By.cssSelector(".row .description h3 a");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToCart() {
        driver.get(CART_URL);
    }

    public boolean isProductInCart(String productName) {
        List<String> names = getCartItemNames();
        return names.stream().anyMatch(name -> name.contains(productName));
    }

    public List<String> getCartItemNames() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cartItems));
        List<WebElement> items = driver.findElements(cartItems);
        return items.stream()
            .map(el -> el.getAttribute("textContent"))
            .map(String::trim)
            .collect(Collectors.toList());
    }
}
