package cz.mandalorian.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumTest {
    private WebDriver driver;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    public void googleSearch()
    {
        // GIVEN
        driver.get("https://www.google.com/");

        //WebDriverWait wait = new WebDriverWait(driver , 10);
        //wait.until(driver -> driver.switchTo().frame(0));
        //WebElement agree = driver.findElement(By.id("introAgreeButton"));
        //agree.click();  // AGREE


        // WHEN
        WebElement searchInput = driver.findElement(By.name("q"));
        searchInput.sendKeys("koloběžka");
        searchInput.sendKeys(Keys.ENTER);
        searchInput.sendKeys();

        // THEN
        Assert.assertTrue(driver.getTitle().startsWith("koloběžka - "));
        Assert.assertTrue(driver.getTitle().startsWith("koloběžka - "));

        Assert.assertTrue(driver.getCurrentUrl().startsWith("https://www.google.com/search?"));
        Assert.assertTrue(driver.getCurrentUrl().contains("q=kolob%C4%9B%C5%BEka"));
    }

    @After
    public void tearDown(){
        if (driver != null) {
            driver.close();
        }
    }
}
