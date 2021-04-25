package cz.mandalorian.testframework;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    private void clickAddTaskBtn() {
        getAddTaskBtn().click();
    }

    private WebElement getSaveTaskBtn() {

        return driver.findElement(By.xpath("//button[text()='Save']"));
    }

    private void clickSaveTaskBtn() {
        getSaveTaskBtn().click();
    }


    private WebElement getTaskInfoHeadline() {
        return driver.findElement(By.cssSelector(".modal-title"));
    }

    public void checkTaskCreationFormOpen() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Save']")));
    }

    public void createTask(TaskTypeType typeType, String name, TaskStatusType statusType, TaskPriorityType priorityType, String description) {
        clickAddTaskBtn();
        checkTaskCreationFormOpen();

        selectTaskType(typeType);
        fillName(name);
        selectStatus(statusType);
        selectPriority(priorityType);
        fillDescription(description);

        clickSaveTaskBtn();
    }


    private void selectPriority(TaskPriorityType priorityType) {
        Select comboBox = new Select(driver.findElement(By.cssSelector("#fields_170")));
        comboBox.selectByVisibleText(priorityType.name());
    }

    private void selectStatus(TaskStatusType statusType) {
        Select comboBox = new Select(driver.findElement(By.cssSelector("#fields_169")));
        comboBox.selectByVisibleText(statusType.name());
    }

    private void selectTaskType(TaskTypeType typeType) {
        Select comboBox = new Select(driver.findElement(By.cssSelector("#fields_167")));
        comboBox.selectByVisibleText(typeType.name());
    }

    private void fillName(String name) {
        WebElement nameInput = driver.findElement(By.cssSelector("#fields_168"));
        nameInput.sendKeys(name);
    }

    private void fillDescription(String description) {
        driver.switchTo().frame(0);
        WebElement descriptionInput = driver.findElement(By.cssSelector(".cke_editable"));
//        descriptionInput.click();
        descriptionInput.sendKeys(description);
        driver.switchTo().defaultContent();
    }

    public void checkTaskContent(TaskTypeType typeType, String name, TaskStatusType statusType, TaskPriorityType priorityType, String description) {
        clickTaskInfoBtn();
        checkTaskInfoOpen();

        Assert.assertEquals(description,getDescriptionText());
        Assert.assertEquals(name,getNameText());

        List<WebElement> taskInfoTableRows = getTaskInfoTableRows();

        Assert.assertEquals(typeType.name(),getTaskTypeText(taskInfoTableRows));
        Assert.assertEquals(statusType.name(),getStatusText(taskInfoTableRows));
        Assert.assertEquals(priorityType.name(),getPriorityText(taskInfoTableRows));
    }

    private String getDescriptionText(){
        return driver.findElement(By.cssSelector(".form-group-172 > .content_box_content")).getText();
    }

    private String getNameText(){
        return driver.findElement(By.cssSelector(".caption")).getText();
    }

    private List<WebElement> getTaskInfoTableRows() {

        return driver.findElements(By.cssSelector("table tbody tr"));
    }

    private String getStatusText(List<WebElement> taskInfoTableRows) {
        return taskInfoTableRows.get(4).findElement(By.tagName("td")).getText();
    }

    private String getTaskTypeText(List<WebElement> taskInfoTableRows) {
        return taskInfoTableRows.get(3).findElement(By.tagName("td")).getText();
    }

    private String getPriorityText(List<WebElement> taskInfoTableRows) {
        return taskInfoTableRows.get(5).findElement(By.tagName("td")).getText();
    }


    private WebElement getTaskInfoBtn() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("table tbody tr")));

        return driver.findElement(By.cssSelector(".fa-info"));
    }

    private void clickTaskInfoBtn() {
        getTaskInfoBtn().click();
    }

    private void checkTaskInfoOpen() {
        List<WebElement> headingList = driver.findElements(By.cssSelector(".media-heading"));

        if(! headingList.isEmpty() ){
            Assert.assertEquals("Description",headingList.get(0).getText());
            Assert.assertEquals("Info",headingList.get(1).getText());
            Assert.assertEquals("Time",headingList.get(2).getText());
        }

    }


    private void clickTasksBreadcrumb(){
        driver.findElement(By.linkText("Tasks")).click();
    }

    private List<WebElement> getTaskTableRows() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("table tbody tr")));


        return driver.findElements(By.cssSelector("table tbody tr"));
    }

    public void taskDelete(String taskName){
        clickTasksBreadcrumb();

        List<WebElement> taskTable = getTaskTableRows();
        WebElement taskRow = findTaskRowInTaskTable(taskName, taskTable);
        List<WebElement> columnsList = taskRow.findElements(By.cssSelector("td"));

        WebElement actionColumn = columnsList.get(1);
        WebElement trashBtn = actionColumn.findElement(By.cssSelector(".fa-trash-o"));
        trashBtn.click();

        checkTaskDeletionFormOpen();
        confirmTaskDeletion();

    }
    private void checkTaskDeletionFormOpen() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Delete']")));
    }
    private void confirmTaskDeletion(){
        List<WebElement> checkboxList = driver.findElements(By.cssSelector("#uniform-delete_confirm"));
        if (!checkboxList.isEmpty()) {
            checkboxList.get(0).click();
        }
        driver.findElement(By.xpath("//button[text()='Delete']")).click();

    }

    public void checkTaskDeleted(String taskName){
        List<WebElement> taskTable = getTaskTableRows();
        WebElement taskRow = findTaskRowInTaskTable(taskName, taskTable);

        Assert.assertNull(taskRow);


    }
    private WebElement findTaskRowInTaskTable(String taskName, List<WebElement> taskTable){
        for (WebElement taskRow: taskTable
        ) {
            if (taskRow.getText().contains(taskName)){
                return taskRow;
            }
        }
        return null;
    }

}


