package com.example.ubernet.e2e.tests;

import com.example.ubernet.e2e.pages.DashboardPage;
import com.example.ubernet.e2e.pages.HomePage;
import com.example.ubernet.e2e.pages.MapPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderRideTest extends TwoChromesTestBase {

    @Test
    public void orderRide() {
        initializeIncognitoWebDriver();
        initializeWebDriver();

        loginDriver();
        loginCustomer();
        orderRideCustomer();

        driver.quit();
        incognitoDriver.quit();
    }

    private void orderRideCustomer() {
        MapPage mapPage = new MapPage(driver);

        mapPage.enterStopValue(0, "Branka Bajica 10, Novi Sad");
        mapPage.enterStopValue(1, "Limanska pijaca, Novi Sad");

        mapPage.searchDirections();
        mapPage.waitForSpinner();
        mapPage.carStep();
        mapPage.friendsStep();
        mapPage.summaryStep();

        String snackBarMessage = mapPage.getSnackBarMessage();
        assertEquals("Successfully reserved ride", snackBarMessage);
    }

    private void loginCustomer() {
        HomePage homePage = new HomePage(driver);

        homePage.setEmail("customer@gmail.com");
        homePage.setPassword("customer");
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
