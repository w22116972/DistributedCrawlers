package ecommerce;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverFactory {
    private static final String DRIVER_CONFIG = "webdriver.chrome.driver";
    private static final String DRIVER_PATH = "src/main/resources/chromedriver";
    private WebDriver driver;

    public WebDriverFactory() {
        System.setProperty(DRIVER_CONFIG, DRIVER_PATH);
        driver = new ChromeDriver();
    }

    public WebDriver createDriver() {
        return this.driver;
    }

}
