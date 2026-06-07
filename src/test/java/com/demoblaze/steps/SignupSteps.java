package com.demoblaze.steps;

import com.demoblaze.pages.SignupPage;
import com.demoblaze.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignupSteps {
    private SignupPage signupPage;

    @When("me registro con usuario {string} y contraseña {string}")
    public void registroUsuario(String user, String pass) {
        if (signupPage == null) {
            signupPage = new com.demoblaze.pages.SignupPage(DriverFactory.getDriver());
        }
        // Generar un usuario único solo cuando se indique el placeholder 'nuevoUser'
        String usernameToUse = user;
        if ("nuevoUser".equals(user)) {
            usernameToUse = user + System.currentTimeMillis();
        }
        // Para el caso 'testuser' queremos garantizar el comportamiento "usuario existente",
        // así que si el valor es 'testuser' creamos primero el usuario y luego intentamos crearlo de nuevo.
        signupPage.openSignupModal();
        if ("testuser".equals(user)) {
            // primer intento para asegurar que el usuario exista
            signupPage.signup(usernameToUse, pass);
            try { signupPage.acceptAlert(); } catch (Exception ignored) {}
            // volver a abrir modal y realizar el intento que debe fallar
            signupPage.openSignupModal();
        }
        signupPage.signup(usernameToUse, pass);
    }

    @Then("debo ver el mensaje de éxito {string}")
    public void verificarMensajeExito(String esperado) {
        String actual = signupPage.getAlertMessage();
        assertEquals(esperado, actual);
        signupPage.acceptAlert();
    }

    @After
    public void cerrarNavegador() {
        DriverFactory.quitDriver();
    }
}
