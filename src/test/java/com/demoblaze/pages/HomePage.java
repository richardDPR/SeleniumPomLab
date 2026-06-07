package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By phonesCategory = By.linkText("Phones");
    private By laptopsCategory = By.linkText("Laptops");
    private By monitorsCategory = By.linkText("Monitors");
    private By productCards = By.cssSelector(".card-title");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get("https://www.demoblaze.com/index.html");
    }

    public void selectCategory(String category) {
        By locator;
        switch (category.toLowerCase()) {
            case "phones":
                locator = phonesCategory;
                break;
            case "laptops":
                locator = laptopsCategory;
                break;
            case "monitors":
                locator = monitorsCategory;
                break;
            default:
                throw new IllegalArgumentException("Categoría no válida: " + category);
        }
        driver.findElement(locator).click();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productCards));
    }

    public List<String> getProductTitles() {
        List<WebElement> products = driver.findElements(productCards);
        return products.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
}
