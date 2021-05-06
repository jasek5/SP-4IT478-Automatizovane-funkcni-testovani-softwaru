package cz.mandalorian.testframework;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class LoginPage extends Page {

    public static final String RELATIVE_URL = "index.php?module=users/login";

    public LoginPage(WebDriver driver) {
        super(driver);

    }

    @Override
    public String open() {
        driver.get(BASE_URL + RELATIVE_URL);
        return driver.getCurrentUrl();
    }

    public DashboardPage login(String username, String password) {
        WebElement usernameInput = driver.findElement(By.cssSelector("input[name='username']"));
        WebElement passInput = driver.findElement(By.cssSelector("input[name='password']"));
        WebElement loginBtn = driver.findElement(By.cssSelector("#login_form > div.form-actions > button"));
//    WebElement remeberMe = driver.findElement();
        usernameInput.sendKeys(username);
        passInput.sendKeys(password);

        loginBtn.click();
        return new DashboardPage(driver);
    }

    public void checkPageOpen() {
        Assert.assertEquals(BASE_URL + RELATIVE_URL, driver.getCurrentUrl());
        Assert.assertNotNull(getLoginForm());

    }

    public WebElement getLoginForm() {

        List<WebElement> loginFormList = driver.findElements(By.cssSelector("#login_form"));
        if (loginFormList.size() >= 1) {
            return loginFormList.get(0);
        }
        //! When array is empty, this should be bad behaviour.
        return null;
    }

    public WebElement getInvalidAlert() {
        List<WebElement> alertList = driver.findElements(By.cssSelector(".alert.alert-danger"));
        if (alertList.size() >= 1) {
            return alertList.get(0);
        }
        //! When array is empty, this should be bad behaviour.
        return null;
    }

    public void checkLoginFailure() {
        Assert.assertNotNull(getInvalidAlert());
        Assert.assertNotNull(getLoginForm());
    }
}


