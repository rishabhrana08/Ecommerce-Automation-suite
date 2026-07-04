package com.rishabh.framework.components;

import com.rishabh.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FooterComponent extends BasePage {

    private final By subscribeEmailInput = By.id("susbscribe_email"); // yes, typo is on the real site
    private final By subscribeButton = By.id("subscribe");
    private final By subscribeSuccessMsg = By.id("success-subscribe");
    private final By scrollUpArrow = By.id("scrollUp");

    public FooterComponent(WebDriver driver) {
        super(driver);
    }

    public void subscribe(String email) {
        scrollTo(subscribeEmailInput);
        type(subscribeEmailInput, email);
        click(subscribeButton);
    }

    public boolean isSubscribeSuccessVisible() {
        return isDisplayed(subscribeSuccessMsg);
    }

    public String getSubscribeSuccessText() {
        return getText(subscribeSuccessMsg);
    }

    public boolean isScrollUpArrowVisible() {
        return isDisplayedQuick(scrollUpArrow, 5);
    }

    public void clickScrollUpArrow() {
        click(scrollUpArrow);
    }
}
