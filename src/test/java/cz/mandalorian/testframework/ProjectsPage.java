package cz.mandalorian.testframework;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ProjectsPage extends Page {

    public static final String RELATIVE_URL = "index.php?module=items/items&path=21";

    public ProjectsPage(WebDriver driver) {
        super(driver);


    }
    public void addProject(String name,PriorityType priorityType, StatusType statusType)  {
        clickAddProjectButton();
        selectPriority(priorityType);
        selectStatus(statusType);
        fillName(name);
        clickSaveProjectButton();




    }

    @Override
    public String open() {
        driver.get(BASE_URL + RELATIVE_URL);
        return driver.getCurrentUrl();
    }

    private void fillName(String name){
        WebElement nameInput = driver.findElement(By.cssSelector("#fields_158"));
        nameInput.sendKeys(name);
    }
    private void selectPriority(PriorityType priorityType){
        Select comboBox = new Select(driver.findElement(By.cssSelector("#fields_156")));
        comboBox.selectByVisibleText(priorityType.name());
    }

    private void selectStatus(StatusType statusType){
        Select comboBox = new Select(driver.findElement(By.cssSelector("#fields_157")));
        comboBox.selectByVisibleText(statusType.name());
    }
    public void checkPageOpen() {
        Assert.assertEquals(BASE_URL + RELATIVE_URL, driver.getCurrentUrl());
        Assert.assertEquals("Projects", getPageTitle().getText());

    }

    private void clickAddProjectButton(){
        WebElement addProjectBtn = driver.findElement(By.xpath("//button[text()='Add Project']"));
        addProjectBtn.click();
        checkProjectCreationFormOpen();

    }

    private void clickSaveProjectButton(){
        WebElement saveProjectBtn = driver.findElement(By.xpath("//button[text()='Save']"));
        saveProjectBtn.click();

    }
    public void checkNameAlert(){
        Assert.assertNotNull(getNameAlert());
    }

    private WebElement getNameAlert(){
        List<WebElement> alertList = driver.findElements(By.cssSelector("#fields_158-error"));

        if (alertList.size() >= 1) {
            return alertList.get(0);
        }

        return null;
    }


    public void checkProjectCreationFormOpen()  {
        WebDriverWait wait = new WebDriverWait(driver, 5);
//        List<WebElement> menuForm = driver.findElements(By.cssSelector("#items_form"));
//        wait.until(ExpectedConditions.invisibilityOfAllElements(menuForm));

//        Assert.assertFalse(menuForm.isEmpty());
//        Thread.sleep(5000);
//        WebElement saveBtn = driver.findElement(By.xpath("//button[text()='Save']"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Save']")));
    }

    private WebElement getPageTitle() {

        return driver.findElement(By.cssSelector(".page-title"));
    }

}
