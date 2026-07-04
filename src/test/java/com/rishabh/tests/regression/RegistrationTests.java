package com.rishabh.tests.regression;

import com.rishabh.framework.testdata.UserData;
import com.rishabh.framework.utils.RandomDataUtil;
import com.rishabh.tests.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Ecommerce Automation")
@Feature("User Registration")
public class RegistrationTests extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "TC01 - Register a brand new user with valid details")
    @Description("Signup with a fresh name/email, fill the account info form and confirm the account gets created")
    public void registerUser_withValidDetails_accountCreatedSuccessfully() {
        UserData user = UserData.builder().build();

        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().signup(user.getName(), user.getEmail());

        Assert.assertTrue(pages.getSignupPage().isLoaded(), "Account info form did not load after signup");
        pages.getSignupPage().fillAccountInformation(user);
        pages.getSignupPage().submitAccountCreation();

        Assert.assertTrue(pages.getSignupPage().isAccountCreatedVisible(), "Account Created message not shown");
        pages.getSignupPage().clickContinue();
        Assert.assertTrue(pages.getHomePage().header().isUserLoggedIn(), "User should be auto logged-in post signup");
    }

    @Test(groups = "regression", description = "TC05 - Signup with an email that's already registered")
    public void registerUser_withExistingEmail_showsAlreadyExistError() {
        UserData user = registerAndLoginNewUser();
        pages.getHomePage().header().logout();

        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().signup(RandomDataUtil.fullName(), user.getEmail());

        Assert.assertTrue(pages.getLoginPage().isSignupErrorVisible(), "Expected 'already exist' error for duplicate email");
        Assert.assertTrue(pages.getLoginPage().getSignupErrorText().toLowerCase().contains("already exist"));
    }

    @Test(groups = "regression", description = "Signup box rejects an obviously invalid email format")
    public void registerUser_withInvalidEmailFormat_signupBlocked() {
        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().signup(RandomDataUtil.fullName(), "not-an-email");

        // browser-native HTML5 validation should keep us on /login, account form never appears
        Assert.assertFalse(pages.getSignupPage().isLoaded(), "Signup should not proceed with a malformed email");
    }

    @Test(groups = "regression", description = "Signup blocked when name field is left empty")
    public void registerUser_withEmptyName_signupBlocked() {
        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().signup("", RandomDataUtil.uniqueEmail());
        Assert.assertFalse(pages.getSignupPage().isLoaded());
    }

    @Test(groups = "regression", description = "Signup blocked when email field is left empty")
    public void registerUser_withEmptyEmail_signupBlocked() {
        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().signup(RandomDataUtil.fullName(), "");
        Assert.assertFalse(pages.getSignupPage().isLoaded());
    }

    @Test(groups = "regression", description = "Account creation form rejects a missing mandatory first name")
    public void registerUser_missingFirstName_createAccountBlocked() {
        UserData user = UserData.builder().firstName("").build();

        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().signup(user.getName(), user.getEmail());
        pages.getSignupPage().fillAccountInformation(user);
        pages.getSignupPage().submitAccountCreation();

        Assert.assertFalse(pages.getSignupPage().isAccountCreatedVisible(),
                "Account shouldn't be created without a first name");
    }

    @Test(groups = "regression", description = "Register selecting the 'Mrs' title and confirm it's accepted")
    public void registerUser_withMrsTitle_accountCreatedSuccessfully() {
        UserData user = UserData.builder().male(false).build();

        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().signup(user.getName(), user.getEmail());
        pages.getSignupPage().fillAccountInformation(user);
        pages.getSignupPage().submitAccountCreation();

        Assert.assertTrue(pages.getSignupPage().isAccountCreatedVisible());
    }

    @Test(groups = "regression", description = "Register opting in to the newsletter")
    public void registerUser_withNewsletterOptIn_accountCreatedSuccessfully() {
        UserData user = UserData.builder().subscribeNewsletter(true).build();

        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().signup(user.getName(), user.getEmail());
        pages.getSignupPage().fillAccountInformation(user);
        pages.getSignupPage().submitAccountCreation();

        Assert.assertTrue(pages.getSignupPage().isAccountCreatedVisible());
    }

    @Test(groups = "regression", description = "Register opting out of the newsletter")
    public void registerUser_withoutNewsletterOptIn_accountCreatedSuccessfully() {
        UserData user = UserData.builder().subscribeNewsletter(false).build();

        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().signup(user.getName(), user.getEmail());
        pages.getSignupPage().fillAccountInformation(user);
        pages.getSignupPage().submitAccountCreation();

        Assert.assertTrue(pages.getSignupPage().isAccountCreatedVisible());
    }

    @Test(groups = "regression", description = "Header shows the correct username right after account creation")
    public void registerUser_verifyLoggedInUsernameMatchesSignup() {
        UserData user = registerAndLoginNewUser();
        Assert.assertEquals(pages.getHomePage().header().getLoggedInUserName(), user.getName());
    }

    @Test(groups = "regression", description = "Long address values are accepted without truncation on the account form")
    public void registerUser_withLongAddressValues_accountCreatedSuccessfully() {
        String longAddress = "Flat 42, Sunrise Apartments, Behind Central Mall, MG Road Extension";
        UserData user = UserData.builder().address(longAddress).build();

        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().signup(user.getName(), user.getEmail());
        pages.getSignupPage().fillAccountInformation(user);
        pages.getSignupPage().submitAccountCreation();

        Assert.assertTrue(pages.getSignupPage().isAccountCreatedVisible());
    }

    @Test(groups = {"smoke", "regression"}, description = "TC01 continued - delete the account after creating it")
    public void deleteAccount_afterRegistration_accountDeletedSuccessfully() {
        registerAndLoginNewUser();
        pages.getHomePage().header().deleteAccount();
        Assert.assertTrue(pages.getSignupPage().isAccountDeletedVisible(), "Account Deleted confirmation not shown");
        pages.getSignupPage().clickContinue();
    }
}
