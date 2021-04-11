package cz.mandalorian.testframework;

import org.openqa.selenium.WebDriver;


public abstract class Page {
    protected WebDriver driver;

    public static final String BASE_URL = "http://digit107.wwwnlss4.a2hosted.com/rukovoditel/";

    public Page(WebDriver driver) {
        this.driver = driver;
    }

    public String open(){
        driver.get(BASE_URL);

        return driver.getCurrentUrl();
    }

}
