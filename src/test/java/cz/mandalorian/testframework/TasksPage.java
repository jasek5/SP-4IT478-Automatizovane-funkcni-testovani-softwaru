package cz.mandalorian.testframework;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TasksPage extends Page {


    public TasksPage(WebDriver driver) {
        super(driver);
    }

    public void checkPageOpen(String projectName) {
        Assert.assertNotNull(getProjectNameBreadCrumb(projectName));
        Assert.assertNotNull(getAddTaskBtn());
    }

    private WebElement getProjectNameBreadCrumb(String projectName) {

        List<WebElement> projectNameBreadCrumbList = driver.findElements(By.linkText(projectName));
        if (projectNameBreadCrumbList.size() >= 1) {
            return projectNameBreadCrumbList.get(0);
        }

        return null;
    }

    private WebElement getAddTaskBtn() {

        return driver.findElement(By.xpath("//button[text()='Add Task']"));
    }


}
