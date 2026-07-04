package com.rishabh.framework.base;

import com.rishabh.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * Base class for every page object. Deliberately thin - the point is that
 * subclasses never reach into driver.findElement(...).click() themselves,
 * everything routes through the wait-wrapped helpers below.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils waitUtils;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    protected void click(By locator) {
        waitUtils.click(locator);
    }

    protected void click(org.openqa.selenium.WebElement element) {
        waitUtils.clickElement(element);
    }

    protected void type(By locator, String text) {
        waitUtils.type(locator, text);
    }

    protected String getText(By locator) {
        return waitUtils.getText(locator);
    }

    protected boolean isDisplayed(By locator) {
        return waitUtils.isVisible(locator);
    }

    protected boolean isDisplayedQuick(By locator, int timeoutSeconds) {
        return waitUtils.isVisible(locator, timeoutSeconds);
    }

    protected List<org.openqa.selenium.WebElement> allVisible(By locator) {
        return waitUtils.waitForAllVisible(locator);
    }

    protected void scrollTo(By locator) {
        waitUtils.scrollIntoView(locator);
    }

    protected void selectDropdownByVisibleText(By locator, String visibleText) {
        org.openqa.selenium.WebElement el = waitUtils.waitForVisible(locator);
        new org.openqa.selenium.support.ui.Select(el).selectByVisibleText(visibleText);
    }

    protected void acceptAlertIfPresent() {
        try {
            waitUtils.fluentWait(d -> d.switchTo().alert() != null, 200);
            driver.switchTo().alert().accept();
        } catch (Exception ignored) {
            // no alert showed up, nothing to do
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void navigateTo(String url) {
        driver.get(url);
        waitUtils.waitForJsAndJQueryToLoad();
    }
}
