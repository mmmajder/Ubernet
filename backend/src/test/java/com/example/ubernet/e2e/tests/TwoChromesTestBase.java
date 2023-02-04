package com.example.ubernet.e2e.tests;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TwoChromesTestBase {

    public static WebDriver driver;
    public static WebDriver incognitoDriver;

    public void initializeIncognitoWebDriver() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("incognito");
        options.setAcceptInsecureCerts(true);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);

        incognitoDriver = new ChromeDriver(options);
        incognitoDriver.manage().window().maximize();
    }

    public void initializeWebDriver() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
}
