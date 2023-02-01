package com.example.ubernet.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
public class HomePage {
    private final WebDriver driver;

    private final static String PAGE_URL = "http://localhost:4200/";

    @FindBy(id = "loginBtn")
    private WebElement loginButton;

    @FindBy(id = "emailInput")
    private WebElement emailInput;

    @FindBy(id = "passwordInput")
    private WebElement passwordInput;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);

        PageFactory.initElements(driver, this);
    }

    public void setEmail(String s) {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.elementToBeClickable(emailInput));
        emailInput.sendKeys(s);
    }

    public void setPassword(String s) {
        passwordInput.sendKeys(s);
    }

    public void clickLoginBtn() {
        loginButton.click();
    }
}
