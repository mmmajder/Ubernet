package com.example.ubernet.e2e.tests;

import com.example.ubernet.e2e.pages.DashboardPage;
import com.example.ubernet.e2e.pages.HomePage;
import com.example.ubernet.e2e.pages.MapPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RideEndTest extends TwoChromesTestBase  {

    @Test
    @DisplayName("Customer orders. Uber arrives, starts and finishes ride. After that uber is free.")
    public void endRide() {
        initializeIncognitoWebDriver();
        initializeWebDriver();

        loginDriver();
        loginCustomer("customer@gmail.com", "customer");
        orderRideCustomer();
        simulateRide();

        driver.quit();
        incognitoDriver.quit();
    }

    @Test
    @DisplayName("Customer orders. Uber arrives, starts and finishes ride. After that uber goes to another ride.")
    public void endRideAndUberContinuesToNextRide() {
        initializeIncognitoWebDriver();
        initializeWebDriver();

        loginDriver();
        loginCustomer("customer@gmail.com", "customer");
        orderRideCustomer();

        logoutCustomer();
        loginCustomer("petar@gmail.com", "admin");  // other customer
        orderRideCustomer();
        simulateRide();
        simulateRide();
        driver.quit();
        incognitoDriver.quit();
    }

    @Test
    @DisplayName("Customer orders. Uber arrives, starts and finishes ride. After that user orders another ride.")
    public void endRideAndCustomerThenOrdersAnotherRide() {
        initializeIncognitoWebDriver();
        initializeWebDriver();

        loginDriver();
        loginCustomer("customer@gmail.com", "customer");
        orderRideCustomer();
        simulateRide();
        orderRideCustomer();
        simulateRide();
        driver.quit();
        incognitoDriver.quit();
    }


    private void logoutCustomer() {
        MapPage mapPage = new MapPage(driver);
        mapPage.logout();
    }


    private void simulateRide() {
        MapPage mapPageDriver = new MapPage(incognitoDriver);
        mapPageDriver.acceptRide();
        mapPageDriver.endRide();
    }

    private void orderRideCustomer() {
        MapPage mapPage = new MapPage(driver);

        // successful ordering
        mapPage.goToSearchDirections();
        mapPage.enterStopValue(0, "Novi Sad");
        mapPage.enterStopValue(1, "Prolaz Milosa Hadzica 4, Novi Sad");
        mapPage.searchDirections();
        mapPage.waitForSpinner();
        mapPage.carStep();
        mapPage.passFriendsStep();
        mapPage.goToSummary();
        mapPage.clickRequestRide();
        assertEquals("Successfully reserved ride", mapPage.getSnackBarMessage());
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
