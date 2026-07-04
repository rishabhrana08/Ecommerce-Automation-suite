package com.rishabh.tests.regression;

import com.rishabh.framework.enums.BrowserType;
import com.rishabh.framework.factory.DriverFactory;
import com.rishabh.framework.factory.PageObjectFactory;
import com.rishabh.framework.testdata.UserData;
import com.rishabh.framework.utils.ConfigReader;
import com.rishabh.framework.utils.RandomDataUtil;
import com.rishabh.tests.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("Ecommerce Automation")
@Feature("Login / Logout")
public class LoginLogoutTests extends BaseTest {

    // one account registered up-front and reused by all the login scenarios below,
    // saves us from burning a fresh signup for every single test
    private static String existingEmail;
    private static String existingPassword;
    private static String existingName;

    @BeforeClass(alwaysRun = true)
    public void createReusableAccount() {
        WebDriver setupDriver = DriverFactory.createDriver(BrowserType.CHROME_HEADLESS);
        try {
            setupDriver.get(ConfigReader.getInstance().getBaseUrl());
            PageObjectFactory setupPages = new PageObjectFactory(setupDriver);
            UserData user = UserData.builder().build();

            setupPages.getHomePage().header().goToSignupLogin();
            setupPages.getLoginPage().signup(user.getName(), user.getEmail());
            setupPages.getSignupPage().fillAccountInformation(user);
            setupPages.getSignupPage().submitAccountCreation();
            setupPages.getSignupPage().clickContinue();

            existingEmail = user.getEmail();
            existingPassword = user.getPassword();
            existingName = user.getName();
        } finally {
            setupDriver.quit();
        }
    }

    @Test(groups = {"smoke", "regression"}, description = "TC02 - Login with correct email and password")
    public void loginUser_withValidCredentials_loginSuccessful() {
        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().login(existingEmail, existingPassword);

        Assert.assertTrue(pages.getHomePage().header().isUserLoggedIn());
        Assert.assertEquals(pages.getHomePage().header().getLoggedInUserName(), existingName);
    }

    @Test(groups = {"smoke", "regression"}, description = "TC03 - Login with incorrect password")
    public void loginUser_withIncorrectPassword_showsError() {
        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().login(existingEmail, "WrongPassword@123");

        Assert.assertTrue(pages.getLoginPage().isLoginErrorVisible());
    }

    @Test(groups = "regression", description = "Login attempt with an email nobody registered")
    public void loginUser_withNonExistentEmail_showsError() {
        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().login(RandomDataUtil.uniqueEmail(), "Whatever@123");

        Assert.assertTrue(pages.getLoginPage().isLoginErrorVisible());
        Assert.assertTrue(pages.getLoginPage().getLoginErrorText().toLowerCase().contains("incorrect"));
    }

    @Test(groups = "regression", description = "Login blocked when email field is empty")
    public void loginUser_withEmptyEmail_blocked() {
        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().login("", existingPassword);

        Assert.assertFalse(pages.getHomePage().header().isUserLoggedIn());
    }

    @Test(groups = "regression", description = "Login blocked when password field is empty")
    public void loginUser_withEmptyPassword_blocked() {
        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().login(existingEmail, "");

        Assert.assertFalse(pages.getHomePage().header().isUserLoggedIn());
    }

    @Test(groups = "regression", description = "Malformed email in the login field is rejected client-side")
    public void loginUser_withInvalidEmailFormat_blocked() {
        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().login("not-an-email", existingPassword);

        Assert.assertFalse(pages.getHomePage().header().isUserLoggedIn());
    }

    @Test(groups = "regression", description = "Password check is case sensitive")
    public void loginUser_wrongPasswordCase_showsError() {
        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().login(existingEmail, existingPassword.toUpperCase());

        Assert.assertTrue(pages.getLoginPage().isLoginErrorVisible());
    }

    @Test(groups = {"smoke", "regression"}, description = "TC04 - Logout redirects back to the login page")
    public void logoutUser_afterLogin_redirectsToLoginPage() {
        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().login(existingEmail, existingPassword);
        Assert.assertTrue(pages.getHomePage().header().isUserLoggedIn());

        pages.getHomePage().header().logout();
        Assert.assertTrue(pages.getLoginPage().isLoaded(), "Should land back on the login page after logout");
    }

    @Test(groups = "regression", description = "Login session survives a page refresh")
    public void loginUser_sessionPersistsAfterPageRefresh() {
        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().login(existingEmail, existingPassword);
        Assert.assertTrue(pages.getHomePage().header().isUserLoggedIn());

        driver.navigate().refresh();
        Assert.assertTrue(pages.getHomePage().header().isUserLoggedIn(), "Should still be logged in post-refresh");
    }

    @Test(groups = "regression", description = "Both login and signup forms are present on /login")
    public void loginPage_navigateDirectly_bothFormsVisible() {
        goToHomePage().header().goToSignupLogin();
        Assert.assertTrue(pages.getLoginPage().isLoaded(), "Expected both login and signup forms on this page");
    }
}
