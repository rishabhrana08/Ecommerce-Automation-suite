package com.rishabh.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Every locator interaction in the framework funnels through here. The whole point
 * is that nothing ever calls element.click()/sendKeys() directly against a raw
 * find() - that's what causes the classic "element not interactable" / stale
 * element flakiness on a JS-heavy site like this one.
 */
public class WaitUtils {

    private final WebDriver driver;
    private final int timeoutSeconds;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.timeoutSeconds = ConfigReader.getInstance().getExplicitWaitSeconds();
    }

    // named newWait() and not wait() - Object.wait() is final, can't override it
    private WebDriverWait newWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public WebElement waitForVisible(By locator) {
        return newWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return newWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public List<WebElement> waitForAllVisible(By locator) {
        return newWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public boolean waitForInvisible(By locator) {
        return newWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForUrlContains(String fragment) {
        newWait().until(ExpectedConditions.urlContains(fragment));
    }

    public void waitForTitleContains(String fragment) {
        newWait().until(ExpectedConditions.titleContains(fragment));
    }

    /**
     * click() with a retry on StaleElementReferenceException - re-fetching the element
     * by locator each attempt. Saw this fire a lot on the products grid where the DOM
     * gets re-rendered right as a category filter finishes loading.
     *
     * Also falls back to a JS click if something else (a sticky header, a browser
     * side-panel, an ad overlay) is covering the element and a plain click() gets
     * intercepted - ran into exactly this with a Chrome side-rail widget on the
     * mobile viewport tests.
     */
    public void click(By locator) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                clickElement(waitForClickable(locator));
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
            } catch (org.openqa.selenium.TimeoutException e) {
                // this site is ad-supported and every so often a third-party interstitial
                // (seen one from Adobe mid-run) covers the whole page and eats the wait -
                // ESC dismisses most of these, worth one more try before giving up
                attempts++;
                dismissAnyOverlay();
            }
        }
        // last try, let it throw if it genuinely won't click
        clickElement(waitForClickable(locator));
    }

    private void dismissAnyOverlay() {
        try {
            new org.openqa.selenium.interactions.Actions(driver).sendKeys(org.openqa.selenium.Keys.ESCAPE).perform();
        } catch (Exception ignored) {
            // no overlay to dismiss, nothing to do
        }
    }

    /**
     * Same click-interception fallback as click(By), but for callers that already
     * hold a WebElement (e.g. picked one out of a list by index) instead of a locator.
     * Nothing in the framework should be calling element.click() straight off a
     * findElements() result without going through this.
     */
    public void clickElement(WebElement element) {
        try {
            element.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    public void type(By locator, String text) {
        WebElement el = waitForVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    public String getText(By locator) {
        return waitForVisible(locator).getText();
    }

    public boolean isVisible(By locator, int timeoutOverrideSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutOverrideSeconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public boolean isVisible(By locator) {
        return isVisible(locator, timeoutSeconds);
    }

    public void scrollIntoView(By locator) {
        WebElement el = waitForVisible(locator);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    /**
     * Generic fluent wait for cases the canned ExpectedConditions don't cover, e.g.
     * waiting on jQuery/ajax activity to settle before the next interaction.
     */
    public <T> T fluentWait(Function<WebDriver, T> condition, int pollEveryMillis) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollEveryMillis))
                .ignoring(NoSuchElementException.class);
        return fluentWait.until((ExpectedCondition<T>) condition::apply);
    }

    public void waitForJsAndJQueryToLoad() {
        fluentWait(wd -> {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) wd;
            boolean jsReady = js.executeScript("return document.readyState").toString().equals("complete");
            Object jqActive = null;
            try {
                jqActive = js.executeScript("return window.jQuery != undefined && jQuery.active");
            } catch (Exception ignored) {
                // site doesn't always expose jQuery on every page, that's fine
            }
            boolean jQueryReady = jqActive == null || jqActive.equals(0L) || jqActive.equals(false);
            return jsReady && jQueryReady;
        }, 200);
    }
}
