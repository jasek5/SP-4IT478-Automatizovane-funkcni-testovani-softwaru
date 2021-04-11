package cz.mandalorian.testframework;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class DashboardPage extends Page{

    public static final String RELATIVE_URL = "index.php?module=dashboard/";

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public void checkPageOpen(){
        Assert.assertEquals(BASE_URL + RELATIVE_URL,driver.getCurrentUrl());
        Assert.assertNotNull(getSideBarMenu());

    }

    public WebElement getSideBarMenu(){
        List<WebElement> sidebarList = driver.findElements(By.cssSelector(".page-sidebar-menu"));
        if(sidebarList.size() >= 1){
            return sidebarList.get(0);
        }
    //! When array is empty, this should be bad behaviour.
        return null;
    }
}

