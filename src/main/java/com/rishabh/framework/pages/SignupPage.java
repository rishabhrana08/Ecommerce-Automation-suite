package com.rishabh.framework.pages;

import com.rishabh.framework.base.BasePage;
import com.rishabh.framework.testdata.UserData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * The "Enter Account Information" form at /signup - shows up right after
 * submitting name+email from the login page's signup box.
 */
public class SignupPage extends BasePage {

    private final By titleMr = By.id("id_gender1");
    private final By titleMrs = By.id("id_gender2");
    private final By password = By.id("password");
    private final By daysDropdown = By.id("days");
    private final By monthsDropdown = By.id("months");
    private final By yearsDropdown = By.id("years");
    private final By newsletterCheckbox = By.id("newsletter");
    private final By specialOffersCheckbox = By.id("optin");

    private final By firstName = By.id("first_name");
    private final By lastName = By.id("last_name");
    private final By company = By.id("company");
    private final By address1 = By.id("address1");
    private final By address2 = By.id("address2");
    private final By country = By.id("country");
    private final By state = By.id("state");
    private final By city = By.id("city");
    private final By zipcode = By.id("zipcode");
    private final By mobileNumber = By.id("mobile_number");
    private final By createAccountButton = By.cssSelector("[data-qa='create-account']");

    private final By accountCreatedHeading = By.cssSelector("[data-qa='account-created']");
    private final By accountDeletedHeading = By.cssSelector("[data-qa='account-deleted']");
    private final By continueButton = By.cssSelector("[data-qa='continue-button']");

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(password) && isDisplayed(firstName);
    }

    public void fillAccountInformation(UserData user) {
        click(user.isMale() ? titleMr : titleMrs);
        type(password, user.getPassword());
        selectDropdownByVisibleText(daysDropdown, user.getBirthDay());
        selectDropdownByVisibleText(monthsDropdown, user.getBirthMonth());
        selectDropdownByVisibleText(yearsDropdown, user.getBirthYear());

        if (user.isSubscribeNewsletter()) {
            click(newsletterCheckbox);
        }
        click(specialOffersCheckbox);

        type(firstName, user.getFirstName());
        type(lastName, user.getLastName());
        type(company, user.getCompany());
        type(address1, user.getAddress());
        type(address2, user.getAddress2());
        selectDropdownByVisibleText(country, user.getCountry());
        type(state, user.getState());
        type(city, user.getCity());
        type(zipcode, user.getZipcode());
        type(mobileNumber, user.getMobileNumber());
    }

    public void submitAccountCreation() {
        click(createAccountButton);
    }

    public boolean isAccountCreatedVisible() {
        return isDisplayed(accountCreatedHeading);
    }

    public boolean isAccountDeletedVisible() {
        return isDisplayed(accountDeletedHeading);
    }

    public void clickContinue() {
        click(continueButton);
    }
}
