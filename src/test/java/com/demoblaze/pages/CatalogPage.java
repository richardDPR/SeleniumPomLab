package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CatalogPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public CatalogPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openHome() {
        driver.get("https://www.demoblaze.com/index.html");
    }

    public void selectProduct(String productName) {
        By productLink = By.linkText(productName);
        WebElement product = wait.until(ExpectedConditions.elementToBeClickable(productLink));
        product.click();
    }
}
