package com.example.ubernet.e2e.tests;

import com.example.ubernet.e2e.pages.DashboardPage;
import com.example.ubernet.e2e.pages.HomePage;
import com.example.ubernet.e2e.pages.MapPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class OrderRideTest extends TwoChromesTestBase {

    @Test
    @DisplayName("1. No available drivers and cannot add yourself to split fair")
    public void a_cannotOrderRide() {
        initializeWebDriver();

        loginCustomer("petar@gmail.com", "admin");
        noAvailableDrivers();

        driver.quit();
    }

    @Test
    @DisplayName("2. Reserve ride test with invalid tries")
    public void b_reserveRide() {
        initializeWebDriver();

        loginCustomer("petar@gmail.com", "admin");
        reserveRideCustomer();

        driver.quit();
    }

    @Test
    @DisplayName("3. Customer orders ride 4 times - 1. invalid locations, 2. no money, 2. success, 3. cannot")
    public void c_orderRide() {
        initializeIncognitoWebDriver();
        initializeWebDriver();

        loginDriver();
        loginCustomer("customer@gmail.com", "customer");
        orderRideCustomer();

        driver.quit();
        incognitoDriver.quit();
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

        mapPage.waitForSnackBarToDissapear();
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

        DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("hh:mm a");

        // CASE 1 - reservation too early
        LocalDateTime invalidTime1 = LocalDateTime.now().plusMinutes(10);
        mapPage.reserveRide(invalidTime1.format(formatTime));
        assertEquals("Reservation can be minimum 15 minutes in advance", mapPage.getSnackBarMessage());

        mapPage.waitForSnackBarToDissapear();

        // CASE 1 - reservation too late
        LocalTime invalidTime2 = LocalTime.now().plusHours(6);
        mapPage.reserveRide(invalidTime2.format(formatTime));
        assertEquals("Reservation can be maximum 5 hours in advance", mapPage.getSnackBarMessage());

        mapPage.waitForSnackBarToDissapear();

        // CASE 1 - successful reservation
        LocalTime validTime = LocalTime.now().plusHours(3);
        mapPage.reserveRide(validTime.format(formatTime));
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
