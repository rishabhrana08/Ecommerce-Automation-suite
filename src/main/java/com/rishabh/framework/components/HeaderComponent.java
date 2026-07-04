package com.rishabh.framework.components;

import com.rishabh.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * The top nav bar is present on literally every page, so tests reach into it
 * through whichever page object they're currently on rather than duplicating
 * these locators per page.
 */
public class HeaderComponent extends BasePage {

    private final By homeLink = By.cssSelector("a[href='/']");
    private final By productsLink = By.cssSelector("a[href='/products']");
    private final By cartLink = By.cssSelector("a[href='/view_cart']");
    private final By signupLoginLink = By.cssSelector("a[href='/login']");
    private final By contactUsLink = By.cssSelector("a[href='/contact_us']");
    private final By testCasesLink = By.cssSelector("a[href='/test_cases']");
    private final By loggedInAsText = By.xpath("//a[contains(text(),'Logged in as')]");
    private final By logoutLink = By.cssSelector("a[href='/logout']");
    private final By deleteAccountLink = By.cssSelector("a[href='/delete_account']");
    private final By cartItemCountBadge = By.cssSelector(".fa-shopping-cart");

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    public void goToHome() {
        click(homeLink);
    }

    public void goToProducts() {
        click(productsLink);
    }

    public void goToCart() {
        click(cartLink);
    }

    public void goToSignupLogin() {
        click(signupLoginLink);
    }

    public void goToContactUs() {
        click(contactUsLink);
    }

    public void goToTestCasesPage() {
        click(testCasesLink);
    }

    public void logout() {
        click(logoutLink);
    }

    public void deleteAccount() {
        click(deleteAccountLink);
    }

    public boolean isUserLoggedIn() {
        return isDisplayedQuick(loggedInAsText, 5);
    }

    public String getLoggedInUserName() {
        String raw = getText(loggedInAsText);
        return raw.replace("Logged in as", "").trim();
    }

    public boolean isCartIconVisible() {
        return isDisplayed(cartItemCountBadge);
    }
}
