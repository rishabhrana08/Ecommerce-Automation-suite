package com.rishabh.framework.factory;

import com.rishabh.framework.enums.BrowserType;
import com.rishabh.framework.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Hands out one WebDriver per thread so the regression/cross-browser suites can run
 * in parallel via TestNG without stepping on each other's browser sessions.
 */
public class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver createDriver(BrowserType browserType) {
        ConfigReader config = ConfigReader.getInstance();
        WebDriver driver;

        switch (browserType) {
            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (config.isHeadless()) {
                    options.addArguments("-headless");
                }
                driver = new FirefoxDriver(options);
            }
            case EDGE -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = new EdgeOptions();
                if (config.isHeadless()) {
                    options.addArguments("--headless=new");
                }
                driver = new EdgeDriver(options);
            }
            case CHROME_HEADLESS -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless=new", "--disable-gpu", "--window-size=1920,1080");
                driver = new ChromeDriver(options);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-notifications", "--remote-allow-origins=*");
                if (config.isHeadless()) {
                    options.addArguments("--headless=new");
                }
                driver = new ChromeDriver(options);
            }
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWaitSeconds()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeoutSeconds()));
        driverThreadLocal.set(driver);
        log.info("Created {} driver on thread {}", browserType, Thread.currentThread().getId());
        return driver;
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("Driver not initialized for this thread - was createDriver() called in a @BeforeMethod?");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
