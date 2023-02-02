package com.example.ubernet.e2e.tests;

import com.example.ubernet.e2e.pages.DashboardPage;
import com.example.ubernet.e2e.pages.HomePage;
import org.testng.annotations.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class LoginTest extends TestBase {

    @Test
    public void loginCustomer() {
        HomePage homePage = new HomePage(driver);

        homePage.setEmail("customer@gmail.com");
        homePage.setPassword("customer");
        homePage.clickLoginBtn();

        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.checkIfTokenBtnExists();
    }

    @Test
    public void loginDriver() {
        HomePage homePage = new HomePage(driver);

        homePage.setEmail("driver@gmail.com");
        homePage.setPassword("driver");
        homePage.clickLoginBtn();

        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.checkIfDriverActivityToggleExists();
    }

    @Test
    public void loginAdmin() {
        HomePage homePage = new HomePage(driver);

        homePage.setEmail("admin@gmail.com");
        homePage.setPassword("admin");
        homePage.clickLoginBtn();

        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.checkIfAnalyticsHeaderExists();
    }

    @Test
    public void failedLogin() {
        HomePage homePage = new HomePage(driver);

        homePage.setEmail("m@gmail.com");
        homePage.setPassword("m");
        homePage.clickLoginBtn();

        DashboardPage dashboardPage = new DashboardPage(driver);
        String message = dashboardPage.getSnackbarText();
        assertEquals("Wrong email or password.", message);
    }

}
