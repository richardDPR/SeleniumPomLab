package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By loginButton = By.id("login2");
    private By usernameField = By.id("loginusername");
    private By passwordField = By.id("loginpassword");
    private By submitButton = By.xpath("//button[text()='Log in']");
    private By welcomeMessage = By.id("nameofuser");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openLoginModal() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(loginButton));
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
    }

    public void login(String user, String pass) {
        //driver.findElement(usernameField).sendKeys(user);
        //driver.findElement(passwordField).sendKeys(pass);
        //driver.findElement(submitButton).click();
        // esperar que los campos estén visibles antes de interactuar
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys(user);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(pass);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(submitButton));
        }
    }

    public String getWelcomeMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeMessage)).getText();
    }

    public String getAlertMessage() {
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }

    public void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }
    
}
