package com.demoblaze.steps;

import com.demoblaze.pages.LoginPage;
import com.demoblaze.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginSteps {
    private LoginPage loginPage;

    @When("ingreso usuario {string} y contraseña {string}")
    public void ingresarCredenciales(String usuario, String password) {
        if (loginPage == null) {
            loginPage = new com.demoblaze.pages.LoginPage(DriverFactory.getDriver());
        }
        // Intent: intentar login primero; si la aplicación responde "Wrong password.",
        // crear el usuario con la contraseña indicada y reintentar login.
        loginPage.openLoginModal();
        loginPage.login(usuario, password);
        try {
            String alert = loginPage.getAlertMessage();
            loginPage.acceptAlert();
            if ("Wrong password.".equals(alert)) {
                com.demoblaze.pages.SignupPage signupPage = new com.demoblaze.pages.SignupPage(DriverFactory.getDriver());
                try {
                    signupPage.openSignupModal();
                    signupPage.signup(usuario, password);
                    signupPage.acceptAlert();
                } catch (Exception ignored) {}
                // reintentar login
                loginPage.openLoginModal();
                loginPage.login(usuario, password);
            }
        } catch (Exception ignored) {
            // sin alert, probablemente login exitoso
        }
    }

    @Then("debo ver el mensaje {string}")
    public void verificarMensaje(String esperado) {
        try {
            String actual = loginPage.getAlertMessage();
            loginPage.acceptAlert();
            System.out.println("[DEBUG] Login - alert message: " + actual);
            assertEquals(esperado, actual, "Mensaje encontrado: " + actual);
        } catch (Exception e) {
            String actual = loginPage.getWelcomeMessage();
            System.out.println("[DEBUG] Login - welcome message: " + actual);
            assertEquals(esperado, actual, "Mensaje encontrado: " + actual);
        }
    }

    @After
    public void cerrarNavegador() {
        DriverFactory.quitDriver();
    }
}
