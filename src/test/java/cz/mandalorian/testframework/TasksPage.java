package cz.mandalorian.testframework;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

        Assert.assertEquals(description, getDescriptionText());
        Assert.assertEquals(name, getNameText());

        List<WebElement> taskInfoTableRows = getTaskInfoTableRows();

        Assert.assertEquals(typeType.name(), getTaskTypeText(taskInfoTableRows));
        Assert.assertEquals(statusType.name(), getStatusText(taskInfoTableRows));
        Assert.assertEquals(priorityType.name(), getPriorityText(taskInfoTableRows));
    }

    private String getDescriptionText() {
        return driver.findElement(By.cssSelector(".form-group-172 > .content_box_content")).getText();
    }

    private String getNameText() {
        return driver.findElement(By.cssSelector(".caption")).getText();
    }

    private List<WebElement> getTaskInfoTableRows() {

        return driver.findElements(By.cssSelector(".item-details table tbody tr"));
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

        if (!headingList.isEmpty()) {
            Assert.assertEquals("Description", headingList.get(0).getText());
            Assert.assertEquals("Info", headingList.get(1).getText());
            Assert.assertEquals("Time", headingList.get(2).getText());
        }

    }


    private void clickTasksBreadcrumb() {
        driver.findElement(By.linkText("Tasks")).click();
    }

    public List<WebElement> getTaskTableRows() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("table tbody tr")));


        return driver.findElements(By.cssSelector("table tbody tr"));
    }

    public void taskDelete(String taskName) {
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

    private void confirmTaskDeletion() {
        List<WebElement> checkboxList = driver.findElements(By.cssSelector("#uniform-delete_confirm"));
        if (!checkboxList.isEmpty()) {
            checkboxList.get(0).click();
        }
        driver.findElement(By.xpath("//button[text()='Delete']")).click();

    }

    public void checkTaskDeleted(String taskName) {
        List<WebElement> taskTable = getTaskTableRows();
        WebElement taskRow = findTaskRowInTaskTable(taskName, taskTable);

        Assert.assertNull(taskRow);


    }

    private WebElement findTaskRowInTaskTable(String taskName, List<WebElement> taskTable) {
        for (WebElement taskRow : taskTable
        ) {
            if (taskRow.getText().contains(taskName)) {
                return taskRow;
            }
        }
        return null;
    }

    public void checkDefaultFilter() {
        WebElement filter = driver.findElement(By.cssSelector(".filters-preview-condition-include"));
        String[] filterConditions = filter.getText().split(", ");

        Assert.assertEquals(3, filterConditions.length);

        for (int i = 0; i < filterConditions.length; i++) {
            String condition = filterConditions[i];

            if (i == 0) {
                Assert.assertEquals(TaskStatusType.New.name(), condition);
            }
            if (i == 1) {
                Assert.assertEquals(TaskStatusType.Open.name(), condition);
            }
            if (i == 2) {
                Assert.assertEquals(TaskStatusType.Waiting.name(), condition);
            }

        }
        checkTasksCount(getTaskTableRows(), 3);


    }

    public void deleteFilterStatusCondition(TaskStatusType... taskStatusTypes) {
        WebElement filter = driver.findElement(By.cssSelector(".filters-preview-condition-include"));
        filter.click();

        checkTaskFilterFormOpen();

        List<WebElement> filterConditions = driver.findElements(By.cssSelector(".search-choice"));
        for (TaskStatusType taskStatusType : taskStatusTypes
        ) {
            for (WebElement filterCondition : filterConditions
            ) {
                String conditionName = filterCondition.findElement(By.tagName("span")).getText();
                if (conditionName.equals(taskStatusType.name())) {
                    getFilterConditionDeleteBtn(filterCondition).click();
                }
            }
        }

        clickSaveFilterBtn();
    }

    public void checkTasksCount(List<WebElement> taskTable, int requiredTaskCount) {

        Assert.assertEquals(requiredTaskCount, taskTable.size());
    }

    private WebElement getFilterConditionDeleteBtn(WebElement filterCondition) {

        return filterCondition.findElement(By.cssSelector(".search-choice-close"));

    }

    private void checkTaskFilterFormOpen() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Save']")));
    }

    private WebElement getSaveFilterBtn() {

        return driver.findElement(By.xpath("//button[text()='Save']"));
    }

    private void clickSaveFilterBtn() {
        getSaveFilterBtn().click();
    }

    public void deleteAllFilters() {
        driver.findElement(By.cssSelector("a[title='Remove All Filters']")).click();
    }

    public void checkTasksStatuses(List<WebElement> taskTable, List<TaskStatusType> statusesToContain) {
        for (WebElement taskRow : taskTable) {
            List<WebElement> columnsList = taskRow.findElements(By.cssSelector("td"));
            String status = columnsList.get(6).getText();
            Assert.assertTrue(statusesToContain.stream().anyMatch(statusType -> statusType.name().equals(status)));
        }
    }

    public void deleteAllTasks() {
        WebElement selectAllCheckbox = driver.findElement(By.cssSelector("#uniform-select_all_items"));

        selectAllCheckbox.click();

        Actions deleteAction = new Actions(driver);
        WebElement withSelectedDropdown = driver.findElement(By.cssSelector(".entitly-listing-buttons-left  .dropdown-toggle"));
        deleteAction.moveToElement(withSelectedDropdown).build().perform();

        WebDriverWait wait = new WebDriverWait(driver, 5);
//        WebElement deleteButton = driver.findElement(By.cssSelector(".dropdown-menu li:nth-child(1)"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".entitly-listing-buttons-left .dropdown-menu li:nth-child(2)")));

        WebElement deleteButton = driver.findElement(By.cssSelector(".entitly-listing-buttons-left .dropdown-menu li:nth-child(2)"));
        deleteButton.click();

        checkTaskDeletionFormOpen();
        confirmTaskDeletion();
    }

}


