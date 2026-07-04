package com.rishabh.framework.factory;

import com.rishabh.framework.pages.*;
import org.openqa.selenium.WebDriver;

/**
 * Just centralizes page object creation so tests don't sprinkle "new XyzPage(driver)"
 * everywhere - makes it a one line change if a page object constructor ever grows params.
 */
public class PageObjectFactory {

    private final WebDriver driver;

    public PageObjectFactory(WebDriver driver) {
        this.driver = driver;
    }

    public HomePage getHomePage() {
        return new HomePage(driver);
    }

    public LoginPage getLoginPage() {
        return new LoginPage(driver);
    }

    public SignupPage getSignupPage() {
        return new SignupPage(driver);
    }

    public ProductsPage getProductsPage() {
        return new ProductsPage(driver);
    }

    public ProductDetailPage getProductDetailPage() {
        return new ProductDetailPage(driver);
    }

    public CartPage getCartPage() {
        return new CartPage(driver);
    }

    public CheckoutPage getCheckoutPage() {
        return new CheckoutPage(driver);
    }

    public PaymentPage getPaymentPage() {
        return new PaymentPage(driver);
    }

    public ContactUsPage getContactUsPage() {
        return new ContactUsPage(driver);
    }
}
