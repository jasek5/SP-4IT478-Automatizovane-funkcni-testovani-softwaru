package cz.mandalorian.selenium;

import java.io.IOException;

public class Utils {

    public static boolean isHeadlessMode() throws IOException {
        PropertiesReader propertiesReader = new PropertiesReader("app.properties");
        return "headless".equals(propertiesReader.getProperty("mode"));
    }

}
