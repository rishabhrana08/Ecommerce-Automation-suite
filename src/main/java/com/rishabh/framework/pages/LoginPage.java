package com.rishabh.framework.pages;

import com.rishabh.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This one page hosts both the login form and the "New User Signup" name/email
 * form, matching how automationexercise.com actually lays it out at /login.
 */
public class LoginPage extends BasePage {

    private final By loginEmail = By.cssSelector("[data-qa='login-email']");
    private final By loginPassword = By.cssSelector("[data-qa='login-password']");
    private final By loginButton = By.cssSelector("[data-qa='login-button']");
    private final By loginErrorMsg = By.xpath("//p[contains(text(),'incorrect')]");

    private final By signupName = By.cssSelector("[data-qa='signup-name']");
    private final By signupEmail = By.cssSelector("[data-qa='signup-email']");
    private final By signupButton = By.cssSelector("[data-qa='signup-button']");
    private final By signupErrorMsg = By.xpath("//p[contains(text(),'already exist')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(loginEmail) && isDisplayed(signupName);
    }

    public void login(String email, String password) {
        type(loginEmail, email);
        type(loginPassword, password);
        click(loginButton);
    }

    public boolean isLoginErrorVisible() {
        return isDisplayedQuick(loginErrorMsg, 5);
    }

    public String getLoginErrorText() {
        return getText(loginErrorMsg);
    }

    public void signup(String name, String email) {
        type(signupName, name);
        type(signupEmail, email);
        click(signupButton);
    }

    public boolean isSignupErrorVisible() {
        return isDisplayedQuick(signupErrorMsg, 5);
    }

    public String getSignupErrorText() {
        return getText(signupErrorMsg);
    }
}
