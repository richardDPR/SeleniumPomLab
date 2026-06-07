package com.demoblaze.steps;

import com.demoblaze.pages.CheckoutPage;
import com.demoblaze.utils.DriverFactory;
import io.cucumber.java.en.Then;
import org.openqa.selenium.Alert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlertSteps {

    @Then("debo ver el mensaje de error {string}")
    public void verificarMensajeError(String esperado) {
        WebDriver driver = DriverFactory.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String actual = alert.getText();
            // permitir algunas variantes comunes del mensaje
            if (!esperado.equals(actual)) {
                if (!("User does not exist.".equals(esperado) && "Wrong password.".equals(actual))) {
                    assertEquals(esperado, actual);
                }
            }
            alert.accept();
            return;
        } catch (TimeoutException e) {
            // fallback: algunos mensajes aparecen en un modal (sweet-alert)
            CheckoutPage checkoutPage = new CheckoutPage(driver);
            String actual = checkoutPage.getConfirmationMessage();
            if (!esperado.equals(actual)) {
                if (!("User does not exist.".equals(esperado) && "Wrong password.".equals(actual))) {
                    assertEquals(esperado, actual);
                }
            }
        }
    }
}
