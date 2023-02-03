package com.example.ubernet.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class MapPage {
    private final WebDriver driver;

    @FindBy(css = "#search-route")
    private WebElement searchButton;

    @FindBy(css = "#spinner")
    private WebElement spinner;

    @FindBy(css = "mat-radio-button[value='reserve']")
    private WebElement rideType;

    @FindBy(css = "#reservationTime")
    private WebElement reservationTime;

    @FindBy(css = "#routeStep")
    private WebElement routeStep;

    @FindBy(css = "#carTypeStep")
    private WebElement carTypeStep;

    @FindBy(css = "#friendsStep")
    private WebElement friendsStep;

    @FindBy(css = "#summaryStep")
    private WebElement summaryStep;

    @FindBy(css = "#carTypeSelect")
    private WebElement carTypeSelect;

    @FindBy(css = "#addFriendButton")
    private WebElement addFriendButton;

    @FindBy(css = "#mat-option-0")
    private WebElement carTypeSelectOption;

    @FindBy(css = "#route-div > mat-form-field input")
    private List<WebElement> stops;

    @FindBy(css = "#friendsEmailField input")
    private WebElement friendsEmailField;

    @FindBy(css = "#request")
    private WebElement requestButton;

    @FindBy(css = "#request-spinner")
    private WebElement requestSpinner;

    @FindBy(css = "div.mat-mdc-snack-bar-label")
    private WebElement snackBar;

    @FindBy(css = "ngx-material-timepicker-content")
    private WebElement timeDialog;

    @FindBy(xpath = "//button/span[contains(text(), 'Start ride')]/..")
    private WebElement acceptRideBtn;

    @FindBy(xpath = "//button/span[contains(text(), 'End ride')]/..")
    private WebElement endRideBtn;

    @FindBy(id = "notificationBtn")
    private WebElement notificationBtn;

    @FindBy(css = ".mdc-list-item__primary-text")
    private List<WebElement> notifications;

    @FindBy(css = ".mat-mdc-menu-content")
    private WebElement notificationPanel;


    @FindBy(xpath = "//mat-icon[text() = 'logout']")
    private WebElement logoutLink;

    public MapPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void carStep() {
        this.carTypeStep.click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(this.carTypeSelect));
        this.carTypeSelect.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(this.carTypeSelectOption));
        this.carTypeSelectOption.click();
    }

    public void passFriendsStep() {
        this.friendsStep.click();
    }

    public void friendsStep(String email) {
        this.friendsStep.click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(this.friendsEmailField));
        this.friendsEmailField.click();
        this.friendsEmailField.sendKeys(email);
        this.addFriendButton.click();
    }

    public void friendsStep() {
        this.friendsStep.click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(this.friendsEmailField));
    }

    public void summaryStep() {
        goToSummary();
        this.clickRequestRide();
        this.waitForRequestSpinner();
    }

    public void goToSummary() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(this.summaryStep));
        this.summaryStep.click();
    }

    public void reserveRide(String time) {
        goToSummary();
        this.rideType.click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(this.reservationTime));
        this.reservationTime.clear();
        this.reservationTime.sendKeys(time);
        this.requestButton.click();
    }

    public void searchDirections() {
        this.searchButton.click();
    }

    public void enterStopValue(int i, String location) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(this.searchButton));
        this.stops.get(i).clear();
        this.stops.get(i).sendKeys(location);
    }

    public void waitForSpinner() {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOf(this.spinner));
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.invisibilityOf(this.spinner));
    }

    public void waitForRequestSpinner() {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOf(this.requestSpinner));
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.invisibilityOf(this.requestSpinner));
    }

    public String getSnackBarMessage() {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOf(snackBar));
        return snackBar.getText();
    }

    public void clickRequestRide() {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.elementToBeClickable(this.requestButton));
        this.requestButton.click();
    }

    public void waitForSnackBarToDissapear() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.invisibilityOf(snackBar));
    }

    public void goToSearchDirections() {
        this.routeStep.click();
    }

    public void acceptRide() {
        new WebDriverWait(driver, Duration.ofSeconds(10*60))
                .until(ExpectedConditions.elementToBeClickable(acceptRideBtn));
        acceptRideBtn.click();
    }

    public void endRide() {
        new WebDriverWait(driver, Duration.ofSeconds(10*60))
                .until(ExpectedConditions.elementToBeClickable(endRideBtn));
        endRideBtn.click();
    }


    public void logout() {
        logoutLink.click();
    }

}
