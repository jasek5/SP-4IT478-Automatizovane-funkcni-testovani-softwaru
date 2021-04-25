package cz.mandalorian.testframework;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.*;

public class ProjectsPage extends Page {

    public static final String RELATIVE_URL = "index.php?module=items/items&path=21";

    public ProjectsPage(WebDriver driver) {
        super(driver);


    }

    public void addProject(String name, ProjectPriorityType projectPriorityType, ProjectStatusType projectStatusType) {
        clickAddProjectButton();
        selectPriority(projectPriorityType);
        selectStatus(projectStatusType);
        fillName(name);
        clickSaveProjectButton();


    }
    public TasksPage addProject(String name, ProjectPriorityType projectPriorityType, ProjectStatusType projectStatusType, String date) {
        clickAddProjectButton();
        selectPriority(projectPriorityType);
        selectStatus(projectStatusType);
        fillName(name);
        fillDate(date);
        clickSaveProjectButton();

        return new TasksPage(driver);
    }

    private void fillDate(String date) {
        WebElement dateInput = driver.findElement(By.cssSelector("#fields_159"));
        dateInput.sendKeys(date);
    }


    @Override
    public String open() {
        driver.get(BASE_URL + RELATIVE_URL);
        return driver.getCurrentUrl();
    }

    private void fillName(String name) {
        WebElement nameInput = driver.findElement(By.cssSelector("#fields_158"));
        nameInput.sendKeys(name);
    }

    private void selectPriority(ProjectPriorityType projectPriorityType) {
        Select comboBox = new Select(driver.findElement(By.cssSelector("#fields_156")));
        comboBox.selectByVisibleText(projectPriorityType.name());
    }

    private void selectStatus(ProjectStatusType projectStatusType) {
        Select comboBox = new Select(driver.findElement(By.cssSelector("#fields_157")));
        comboBox.selectByVisibleText(projectStatusType.name());
    }

    public void checkPageOpen() {
        Assert.assertEquals(BASE_URL + RELATIVE_URL, driver.getCurrentUrl());
        Assert.assertEquals("Projects", getPageTitle().getText());

    }

    private void clickAddProjectButton() {
        WebElement addProjectBtn = driver.findElement(By.xpath("//button[text()='Add Project']"));
        addProjectBtn.click();
        checkProjectCreationFormOpen();

    }

    private void clickSaveProjectButton() {
        WebElement saveProjectBtn = driver.findElement(By.xpath("//button[text()='Save']"));
        saveProjectBtn.click();

    }

    public void checkNameAlert() {
        Assert.assertNotNull(getNameAlert());
    }

    private WebElement getNameAlert() {
        List<WebElement> alertList = driver.findElements(By.cssSelector("#fields_158-error"));

        if (alertList.size() >= 1) {
            return alertList.get(0);
        }

        return null;
    }


    public void checkProjectCreationFormOpen() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Save']")));
    }

    private WebElement getPageTitle() {

        return driver.findElement(By.cssSelector(".page-title"));
    }

    public String getTodayDate() {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(today);
    }


    public void checkProjectAdded(String projectName, ProjectPriorityType projectPriorityType, ProjectStatusType projectStatusType, String date){
        List<WebElement> projectTable = getProjectTableRows();
        WebElement projectRow = findProjectRowInProjectTable(projectName, projectTable);

        Assert.assertNotNull(projectRow);
        checkProjectRowContent(projectRow, projectPriorityType, projectStatusType,date);


    }
    private List<WebElement> getProjectTableRows() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("table tbody tr")));
        List<WebElement> projectTable =  driver.findElements(By.cssSelector("table tbody tr"));


        return projectTable;
    }

    private WebElement findProjectRowInProjectTable(String projectName, List<WebElement> projectTable){
        for (WebElement projectRow: projectTable
             ) {
            if (projectRow.getText().contains(projectName)){
                return projectRow;
            }
        }
        return null;
    }

    private void checkProjectRowContent(WebElement projectRow, ProjectPriorityType projectPriorityType, ProjectStatusType projectStatusType, String date){
        List<WebElement> columnsList = projectRow.findElements(By.cssSelector("td"));
        String priorityContents = columnsList.get(3).getText();
        String statusContents = columnsList.get(5).getText();
        String dateContents = columnsList.get(6).getText();

        Assert.assertEquals(priorityContents, projectPriorityType.toString());
        Assert.assertEquals(statusContents, projectStatusType.toString());
        Assert.assertEquals(dateContents,dateFormatTransform(date));


    }

    private String dateFormatTransform(String date){
        String newDivider = "/";
        String[] dateParts = date.split("-");
        List<String> datePartList = Arrays.asList(dateParts);
        String year = datePartList.get(0);
        String month = datePartList.get(1);
        String day = datePartList.get(2);
        return month + newDivider + day + newDivider + year;
    }

    public void projectDelete(String projectName){
        List<WebElement> projectTable = getProjectTableRows();
        WebElement projectRow = findProjectRowInProjectTable(projectName, projectTable);
        List<WebElement> columnsList = projectRow.findElements(By.cssSelector("td"));

        WebElement actionColumn = columnsList.get(1);
        WebElement trashBtn = actionColumn.findElement(By.cssSelector(".fa-trash-o"));
        trashBtn.click();

        checkProjectDeletionFormOpen();
        confirmProjectDeletion();

    }
    private void checkProjectDeletionFormOpen() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Delete']")));
    }
    private void confirmProjectDeletion(){
        List<WebElement> checkboxList = driver.findElements(By.cssSelector("#uniform-delete_confirm"));
        if (!checkboxList.isEmpty()) {
            checkboxList.get(0).click();
        }
        driver.findElement(By.xpath("//button[text()='Delete']")).click();

    }

    public void checkProjectDeleted(String projectName){
        List<WebElement> projectTable = getProjectTableRows();
        WebElement projectRow = findProjectRowInProjectTable(projectName, projectTable);

        Assert.assertNull(projectRow);


    }

}
