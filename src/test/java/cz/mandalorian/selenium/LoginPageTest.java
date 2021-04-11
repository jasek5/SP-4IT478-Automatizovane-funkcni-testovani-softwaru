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

public class LoginPageTest {
    private WebDriver driver;
    private String username = "rukovoditel";
    private  String password = "vse456ru";
    private  String invalidPassword = "invalid_password";

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    public void loginPositive()
    {
        // GIVEN
        LoginPage page = new LoginPage(driver);
        page.open();
        page.checkPageOpen();


        // WHEN
        DashboardPage dashboardPage = page.login(username, password);

        // THEN
        dashboardPage.checkPageOpen();
    }
    @Test

    public void loginNegativeWithInvalidPassword() {
        // GIVEN
        LoginPage page = new LoginPage(driver);
        page.open();
        page.checkPageOpen();

        // WHEN
        DashboardPage dashboardPage = page.login(username, invalidPassword);

        // THEN
        page.checkLoginFailure();
        dashboardPage.checkPageNotOpen();
    }


    @After
    public void tearDown(){
        if (driver != null) {
            driver.close();
        }
    }
}


