package cz.mandalorian.selenium;

import cz.mandalorian.testframework.DashboardPage;
import cz.mandalorian.testframework.LoginPage;
import cz.mandalorian.testframework.Page;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DashboardPageTest {
    private WebDriver driver;
//    private String username = "rukovoditel";
//    private String password = "vse456ru";
//    private String invalidPassword = "invalid_password";

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        this.driver = new ChromeDriver(options);
        this.driver.manage().window().maximize();
    }

    /**
     * Logged user logs off.
     */
    @Test
    public void logOff() throws InterruptedException {
        // GIVEN
        LoginPageTest loginPageTest = new LoginPageTest();
        loginPageTest.setDriver(driver);
        loginPageTest.loginPositive();
        DashboardPage dashboardPage = new DashboardPage(driver);

        // WHEN
        dashboardPage.logOff();

        // THEN
        dashboardPage.checkPageNotOpen();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.checkPageOpen();
    }


    @After
    public void tearDown() {
        if (driver != null) {
            driver.close();
        }
    }
}

