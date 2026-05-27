package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CatalogoPage {

    private static final String url_catalogo = "https://sauce-demo.myshopify.com/collections/all";
    private WebDriver driver;
    private WebDriverWait wait;


    public CatalogoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navegar_catalogo() {
        driver.get(url_catalogo);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h3")));           
    }

    public void click_producto(String producto) {
        By objeto = By.id("product-2");
        WebElement opcion = wait.until(ExpectedConditions.elementToBeClickable(objeto));
        opcion.click();
    }
}
