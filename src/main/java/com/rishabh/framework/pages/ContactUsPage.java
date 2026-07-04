package com.rishabh.framework.pages;

import com.rishabh.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ContactUsPage extends BasePage {

    private final By nameInput = By.cssSelector("[data-qa='name']");
    private final By emailInput = By.cssSelector("[data-qa='email']");
    private final By subjectInput = By.cssSelector("[data-qa='subject']");
    private final By messageInput = By.cssSelector("[data-qa='message']");
    private final By fileUploadInput = By.name("upload_file");
    private final By submitButton = By.cssSelector("[data-qa='submit-button']");
    private final By successMsg = By.cssSelector(".status.alert-success");
    private final By homeButton = By.cssSelector(".btn.btn-success");

    public ContactUsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(nameInput) && isDisplayed(submitButton);
    }

    public void fillForm(String name, String email, String subject, String message) {
        type(nameInput, name);
        type(emailInput, email);
        type(subjectInput, subject);
        type(messageInput, message);
    }

    public void attachFile(String absolutePath) {
        waitUtils.waitForVisible(fileUploadInput).sendKeys(absolutePath);
    }

    public void submit() {
        click(submitButton);
        acceptAlertIfPresent(); // site fires a window.confirm() before actually posting
    }

    public boolean isSuccessMessageVisible() {
        return isDisplayed(successMsg);
    }

    public String getSuccessMessageText() {
        return getText(successMsg);
    }

    public void goHome() {
        click(homeButton);
    }
}
