package cz.mandalorian.selenium;


import cz.mandalorian.testframework.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
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
        projectsPage.addProject("", ProjectPriorityType.Urgent, ProjectStatusType.New);
        // THEN
        projectsPage.checkNameAlert();
        projectsPage.checkProjectCreationFormOpen();
    }

    @Test
    public void positiveAddProjectWithTodayDate() {
        //GIVEN
        LoginPageTest loginPageTest = new LoginPageTest();
        loginPageTest.setDriver(driver);
        loginPageTest.loginPositive();
        ProjectsPage projectsPage = new ProjectsPage(driver);
        projectsPage.open();
        projectsPage.checkPageOpen();
        // WHEN
        String uniqueName = Utils.generateUniqueName(PrefixType.Project_Mandalorian_);
        String todayDate = projectsPage.getTodayDate();
        ProjectPriorityType priority = ProjectPriorityType.High;
        ProjectStatusType status = ProjectStatusType.New;

        TasksPage tasksPage = projectsPage.addProject(uniqueName, priority, status, todayDate);


        // THEN
        tasksPage.checkPageOpen(uniqueName);
        projectsPage.open();
        projectsPage.checkPageOpen();
        projectsPage.checkProjectAdded(uniqueName, priority, status, todayDate);
        projectsPage.projectDelete(uniqueName);
        projectsPage.checkProjectDeleted(uniqueName);

    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.close();
        }
    }
}
