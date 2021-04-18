package cz.mandalorian.selenium;


import cz.mandalorian.testframework.*;
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

import java.io.IOException;

public class ProjectsPageTest {
    private WebDriver driver;
//    private String username = "rukovoditel";
//    private String password = "vse456ru";
//    private String invalidPassword = "invalid_password";

    @Before
    public void setUp() throws IOException {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-dev-shm-usage");
        if (Utils.isHeadlessMode()) {
            options.addArguments("--headless");
        }
        this.driver = new ChromeDriver(options);
        this.driver.manage().window().maximize();
    }

    /**
     * Logged user logs off.
     */
    @Test
    public void negativeAddProjectWithoutName() {
        // GIVEN
        LoginPageTest loginPageTest = new LoginPageTest();
        loginPageTest.setDriver(driver);
        loginPageTest.loginPositive();
        ProjectsPage projectsPage = new ProjectsPage(driver);
        projectsPage.open();
        projectsPage.checkPageOpen();

        // WHEN
        projectsPage.addProject("", PriorityType.Urgent, StatusType.New);
        // THEN
        projectsPage.checkNameAlert();
        projectsPage.checkProjectCreationFormOpen();
    }


    @After
    public void tearDown() {
        if (driver != null) {
            driver.close();
        }
    }
}
