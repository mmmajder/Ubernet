package com.example.ubernet.e2e.tests;

import com.example.ubernet.e2e.pages.DashboardPage;
import com.example.ubernet.e2e.pages.HomePage;
import com.example.ubernet.e2e.pages.MapPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderRideTest extends TwoChromesTestBase {

    @Test
    @DisplayName("Customer orders ride 4 times - 1. invalid locations, 2. no money, 2. success, 3. cannot")
    public void orderRide() {
        initializeIncognitoWebDriver();
        initializeWebDriver();

        loginDriver();
        loginCustomer("customer@gmail.com", "customer");
        orderRideCustomer();

        driver.quit();
        incognitoDriver.quit();
    }

    @Test
    @DisplayName("No available drivers and cannot add yourself to split fair")
    public void cannotOrderRide() {
        initializeWebDriver();

        loginCustomer("petar@gmail.com", "admin");
        noAvailableDrivers();

        driver.quit();
    }

    @Test
    @DisplayName("Reserve ride test with invalid tries")
    public void reserveRide() {
        initializeWebDriver();

        loginCustomer("petar@gmail.com", "admin");
        reserveRideCustomer();

        driver.quit();
    }

    private void orderRideCustomer() {
        MapPage mapPage = new MapPage(driver);

        // CASE 1 - invalid locations
        mapPage.enterStopValue(0, "aufbaeuigbaeuig");
        mapPage.enterStopValue(1, "sofbeougbe");
        mapPage.searchDirections();
        assertEquals("Please enter existing locations!", mapPage.getSnackBarMessage());

        // CASE 2 - not enough money
        mapPage.enterStopValue(0, "Branka Bajica 10, Novi Sad");
        mapPage.enterStopValue(1, "Slavija, Beograd");

        mapPage.searchDirections();
        mapPage.waitForSpinner();
        mapPage.carStep();
        mapPage.friendsStep("petar@gmail.com");
        mapPage.summaryStep();

        assertEquals("You do not have enough money to pay for ride", mapPage.getSnackBarMessage());

        // CASE 3 - successful ordering
        mapPage.goToSearchDirections();
        mapPage.enterStopValue(0, "Branka Bajica 10, Novi Sad");
        mapPage.enterStopValue(1, "Limanska pijaca, Novi Sad");
        mapPage.searchDirections();
        mapPage.waitForSpinner();
        mapPage.summaryStep();

        assertEquals("Successfully reserved ride", mapPage.getSnackBarMessage());

        // CASE 4 - customer cannot order ride again
        mapPage.waitForSnackBarToDissapear();
        mapPage.clickRequestRide();
        assertEquals("Active customer can not request another ride!", mapPage.getSnackBarMessage());
    }

    private void noAvailableDrivers() {
        MapPage mapPage = new MapPage(driver);

        mapPage.enterStopValue(0, "Branka Bajica 10, Novi Sad");
        mapPage.enterStopValue(1, "Limanska pijaca, Novi Sad");

        mapPage.searchDirections();
        mapPage.waitForSpinner();
        mapPage.carStep();
        mapPage.friendsStep("petar@gmail.com");
        assertEquals("You cannot add yourself.", mapPage.getSnackBarMessage());

        mapPage.summaryStep();
        assertEquals("There are no available cars at the moment", mapPage.getSnackBarMessage());
    }

    private void reserveRideCustomer() {
        MapPage mapPage = new MapPage(driver);

        mapPage.enterStopValue(0, "Branka Bajica 10, Novi Sad");
        mapPage.enterStopValue(1, "Limanska pijaca, Novi Sad");

        mapPage.searchDirections();
        mapPage.waitForSpinner();
        mapPage.carStep();
        mapPage.friendsStep();

        mapPage.summaryStep();
        LocalTime time = LocalTime.now().plusMinutes(10);
        mapPage.reserveRide(time.getHour(), time.getMinute());

        // check error
    }

    private void loginCustomer(String email, String password) {
        HomePage homePage = new HomePage(driver);

        homePage.setEmail(email);
        homePage.setPassword(password);
        homePage.clickLoginBtn();

        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.checkIfTokenBtnExists();

        dashboardPage.goToMap();
    }

    private void loginDriver() {
        HomePage homePage = new HomePage(incognitoDriver);

        homePage.setEmail("driver@gmail.com");
        homePage.setPassword("driver");
        homePage.clickLoginBtn();

        DashboardPage dashboardPage = new DashboardPage(incognitoDriver);
        dashboardPage.checkIfDriverActivityToggleExists();
    }
}
