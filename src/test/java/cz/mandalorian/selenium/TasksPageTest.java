package cz.mandalorian.selenium;


import cz.mandalorian.testframework.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TasksPageTest {
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

    @Test
    public void positiveAddTaskWithRemoval() {
        //GIVEN
        LoginPageTest loginPageTest = new LoginPageTest();
        loginPageTest.setDriver(driver);
        loginPageTest.loginPositive();

        ProjectsPage projectsPage = new ProjectsPage(driver);
        projectsPage.open();
        projectsPage.checkPageOpen();

        String uniqueName = Utils.generateUniqueName(PrefixType.Project_Mandalorian_);
        String todayDate = projectsPage.getTodayDate();
        ProjectPriorityType priority = ProjectPriorityType.High;
        ProjectStatusType status = ProjectStatusType.New;
        TasksPage tasksPage = projectsPage.addProject(uniqueName, priority, status, todayDate);
        tasksPage.checkPageOpen(uniqueName);

        // WHEN
        TaskTypeType taskType = TaskTypeType.Task;
        String name = Utils.generateUniqueName(PrefixType.Task_Mandalorian_);
        TaskStatusType statusType = TaskStatusType.New;
        TaskPriorityType priorityType = TaskPriorityType.Medium;
        String description = "Mandalorian project task";

        tasksPage.createTask(taskType, name, statusType, priorityType, description);
        // THEN

        tasksPage.checkTaskContent(taskType, name, statusType, priorityType, description);
        tasksPage.taskDelete(name);
        tasksPage.checkTaskDeleted(name);
        projectsPage.open();
        projectsPage.checkPageOpen();
        projectsPage.projectDelete(uniqueName);
        projectsPage.checkProjectDeleted(uniqueName);

    }

    @Test
    public void positiveFilterTasksWithRemoval() {
        //GIVEN
        LoginPageTest loginPageTest = new LoginPageTest();
        loginPageTest.setDriver(driver);
        loginPageTest.loginPositive();

        ProjectsPage projectsPage = new ProjectsPage(driver);
        projectsPage.open();
        projectsPage.checkPageOpen();

        String uniqueName = Utils.generateUniqueName(PrefixType.Project_Mandalorian_);
        String todayDate = projectsPage.getTodayDate();
        ProjectPriorityType priority = ProjectPriorityType.High;
        ProjectStatusType status = ProjectStatusType.New;
        TasksPage tasksPage = projectsPage.addProject(uniqueName, priority, status, todayDate);
        tasksPage.checkPageOpen(uniqueName);

        // WHEN
        tasksPage.createTask(TaskTypeType.Task, Utils.generateUniqueName(PrefixType.Task_Mandalorian_), TaskStatusType.New, TaskPriorityType.Medium, "lobster1");
        tasksPage.createTask(TaskTypeType.Bug, Utils.generateUniqueName(PrefixType.Task_Mandalorian_), TaskStatusType.Open, TaskPriorityType.High, "lobster2");
        tasksPage.createTask(TaskTypeType.Change, Utils.generateUniqueName(PrefixType.Task_Mandalorian_), TaskStatusType.Waiting, TaskPriorityType.Urgent, "lobster3");
        tasksPage.createTask(TaskTypeType.Bug, Utils.generateUniqueName(PrefixType.Task_Mandalorian_), TaskStatusType.Done, TaskPriorityType.Medium, "lobster4");
        tasksPage.createTask(TaskTypeType.Idea, Utils.generateUniqueName(PrefixType.Task_Mandalorian_), TaskStatusType.Closed, TaskPriorityType.High, "lobster5");
        tasksPage.createTask(TaskTypeType.Change, Utils.generateUniqueName(PrefixType.Task_Mandalorian_), TaskStatusType.Paid, TaskPriorityType.Urgent, "lobster6");
        tasksPage.createTask(TaskTypeType.Task, Utils.generateUniqueName(PrefixType.Task_Mandalorian_), TaskStatusType.Canceled, TaskPriorityType.Urgent, "lobster7");


        tasksPage.checkDefaultFilter();
        tasksPage.deleteFilterStatusCondition(TaskStatusType.Open);

        List<WebElement> taskTableRows = tasksPage.getTaskTableRows();
        tasksPage.checkTasksCount(taskTableRows,2);
        tasksPage.checkTasksStatuses(taskTableRows, Arrays.asList(TaskStatusType.New, TaskStatusType.Waiting));

        tasksPage.deleteAllFilters();

        taskTableRows = tasksPage.getTaskTableRows();
        tasksPage.checkTasksStatuses(taskTableRows, Arrays.asList(TaskStatusType.New, TaskStatusType.Waiting,TaskStatusType.Paid, TaskStatusType.Canceled, TaskStatusType.Open,TaskStatusType.Closed,TaskStatusType.Done));
        tasksPage.checkTasksCount(taskTableRows,7);

        tasksPage.deleteAllTasks();
        taskTableRows = tasksPage.getTaskTableRows();

        //No record found
        tasksPage.checkTasksCount(taskTableRows,1);

        //THEN
        projectsPage.open();
        projectsPage.checkPageOpen();
        projectsPage.projectDelete(uniqueName);
        projectsPage.checkProjectDeleted(uniqueName);

    }

    @After
    public void tearDown() {
        if (driver != null) {
//            driver.close();
        }
    }
}

