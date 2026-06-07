package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SignupPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By signupButton = By.id("signin2");
    private By usernameField = By.id("sign-username");
    private By passwordField = By.id("sign-password");
    private By submitButton = By.xpath("//button[text()='Sign up']");

    public SignupPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openSignupModal() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(signupButton)).click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(signupButton));
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
    }

    public void signup(String user, String pass) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys(user);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(pass);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(submitButton));
        }
    }

    public String getAlertMessage() {
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }

    public void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }
}
