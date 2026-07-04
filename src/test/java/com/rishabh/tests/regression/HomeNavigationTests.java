package com.rishabh.tests.regression;

import com.rishabh.framework.utils.RandomDataUtil;
import com.rishabh.tests.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Ecommerce Automation")
@Feature("Home Page & Navigation")
public class HomeNavigationTests extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Home page loads with logo and featured items")
    public void homePage_loadsSuccessfully_logoAndFeaturedItemsVisible() {
        Assert.assertTrue(goToHomePage().isLoaded(), "Home page didn't load as expected");
    }

    @Test(groups = {"smoke", "regression"}, description = "Header 'Products' link navigates to the products page")
    public void homePage_headerNavigation_productsLinkWorks() {
        goToHomePage().header().goToProducts();
        Assert.assertTrue(pages.getProductsPage().isLoaded());
    }

    @Test(groups = "regression", description = "Header cart icon navigates to the cart page")
    public void homePage_headerNavigation_cartLinkWorks() {
        goToHomePage().header().goToCart();
        Assert.assertTrue(driver.getCurrentUrl().contains("/view_cart"));
    }

    @Test(groups = "regression", description = "Header 'Contact us' link navigates to the contact form")
    public void homePage_headerNavigation_contactUsLinkWorks() {
        goToHomePage().header().goToContactUs();
        Assert.assertTrue(pages.getContactUsPage().isLoaded());
    }

    @Test(groups = "regression", description = "TC07 - Test cases page is reachable from the header")
    public void homePage_headerNavigation_testCasesLinkWorks() {
        goToHomePage().header().goToTestCasesPage();
        Assert.assertTrue(driver.getCurrentUrl().contains("/test_cases"));
    }

    @Test(groups = "regression", description = "Category and brand sidebars render on the home page")
    public void homePage_categoryAndBrandSidebars_visible() {
        Assert.assertTrue(goToHomePage().isCategorySidebarVisible());
        Assert.assertTrue(pages.getHomePage().isBrandsSidebarVisible());
    }

    @Test(groups = {"smoke", "regression"}, description = "TC10 - Subscribe with a valid email on the home page")
    public void homeSubscription_validEmail_showsSuccessMessage() {
        goToHomePage().footer().subscribe(RandomDataUtil.uniqueEmail());
        Assert.assertTrue(pages.getHomePage().footer().isSubscribeSuccessVisible());
        Assert.assertTrue(pages.getHomePage().footer().getSubscribeSuccessText()
                .toLowerCase().contains("successfully subscribed"));
    }

    @Test(groups = "regression", description = "Subscribing with an empty email doesn't fire the success banner")
    public void homeSubscription_emptyEmail_blocked() {
        goToHomePage().footer().subscribe("");
        Assert.assertFalse(pages.getHomePage().footer().isSubscribeSuccessVisible());
    }

    @Test(groups = "regression", description = "TC25/26 - Scroll-up arrow shows after scrolling down and jumps back to top")
    public void homePage_scrollDownThenUp_arrowAppearsAndScrollsToTop() {
        goToHomePage();
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

        Assert.assertTrue(pages.getHomePage().footer().isScrollUpArrowVisible());
        pages.getHomePage().footer().clickScrollUpArrow();

        // give the scroll animation a beat, then check we're back near the top
        waitUntilScrollTopIsNear(0, 50);
    }

    private void waitUntilScrollTopIsNear(int target, int tolerance) {
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(d -> {
                    Long scrollY = (Long) ((JavascriptExecutor) d).executeScript("return window.pageYOffset;");
                    return Math.abs(scrollY - target) <= tolerance;
                });
    }
}
