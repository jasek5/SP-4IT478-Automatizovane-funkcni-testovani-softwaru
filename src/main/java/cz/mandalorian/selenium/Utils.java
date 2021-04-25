package cz.mandalorian.selenium;

import java.io.IOException;
import java.util.UUID;

public class Utils {

    public static boolean isHeadlessMode() throws IOException {
        PropertiesReader propertiesReader = new PropertiesReader("app.properties");
        return "headless".equals(propertiesReader.getProperty("mode"));
    }

    public static String generateUniqueName(PrefixType prefix){
        return prefix.name() + UUID.randomUUID().toString();
    }
}
