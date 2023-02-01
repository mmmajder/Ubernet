package com.example.ubernet.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {
    private final WebDriver driver;

    @FindBy(id = "driverActivityToggle")
    private WebElement driverActivityToggle;

    @FindBy(id = "analyticsHeader")
    private WebElement analyticsHeader;

    @FindBy(id = "tokenBtn")
    private WebElement tokenBtn;

    @FindBy(css = "div.mat-mdc-snack-bar-label")
    private WebElement snackBar;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void checkIfDriverActivityToggleExists() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(driverActivityToggle));
    }

    public void checkIfAnalyticsHeaderExists() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(analyticsHeader));
    }

    public void checkIfTokenBtnExists() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(tokenBtn));
    }

    public String getSnackbarText() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(snackBar));
        return snackBar.getText();
    }
}
