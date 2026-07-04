package com.rishabh.tests.base;

import com.rishabh.framework.enums.BrowserType;
import com.rishabh.framework.factory.DriverFactory;
import com.rishabh.framework.factory.PageObjectFactory;
import com.rishabh.framework.pages.HomePage;
import com.rishabh.framework.testdata.UserData;
import com.rishabh.framework.utils.ConfigReader;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Every test class extends this. Browser and viewport come from the TestNG suite
 * XML <parameter> tags so the same test bodies run unchanged across
 * smoke/regression/cross-browser suites - swap the suite file, not the test.
 */
public class BaseTest {

    protected WebDriver driver;
    protected PageObjectFactory pages;
    private final ConfigReader config = ConfigReader.getInstance();

    @Parameters({"browser", "viewportWidth", "viewportHeight"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser,
                       @Optional("1920") String viewportWidth,
                       @Optional("1080") String viewportHeight) {

        BrowserType browserType = resolveBrowser(browser);
        driver = DriverFactory.createDriver(browserType);
        driver.manage().window().setSize(new Dimension(
                Integer.parseInt(viewportWidth), Integer.parseInt(viewportHeight)));

        pages = new PageObjectFactory(driver);
        driver.get(config.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    protected HomePage goToHomePage() {
        return pages.getHomePage();
    }

    /**
     * Full signup -> account info -> continue flow, used by every test module that
     * just needs "a logged in user" as a precondition (cart persistence, checkout etc).
     */
    protected UserData registerAndLoginNewUser() {
        UserData user = UserData.builder().build();

        pages.getHomePage().header().goToSignupLogin();
        pages.getLoginPage().signup(user.getName(), user.getEmail());
        pages.getSignupPage().fillAccountInformation(user);
        pages.getSignupPage().submitAccountCreation();
        pages.getSignupPage().clickContinue();

        return user;
    }

    protected void loginExistingUser(String email, String password) {
        pages.getHomePage().header().goToSignupLogin();
        pages.getLoginPage().login(email, password);
    }

    private BrowserType resolveBrowser(String browser) {
        if (config.isHeadless() && browser.equalsIgnoreCase("chrome")) {
            return BrowserType.CHROME_HEADLESS;
        }
        return switch (browser.toLowerCase()) {
            case "firefox" -> BrowserType.FIREFOX;
            case "edge" -> BrowserType.EDGE;
            default -> BrowserType.CHROME;
        };
    }
}
