package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By placeOrderButton = By.xpath("//button[text()='Place Order']");
    private By nameField = By.id("name");
    private By cardField = By.id("card");
    private By purchaseButton = By.xpath("//button[text()='Purchase']");
    private By confirmationMessage = By.cssSelector(".sweet-alert h2");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openCheckout() {
        driver.findElement(placeOrderButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField));
    }

    public void fillForm(String name, String card) {
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(cardField).sendKeys(card);
        driver.findElement(purchaseButton).click();
    }

    public String getConfirmationMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationMessage)).getText();
    }
}
