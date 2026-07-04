package com.rishabh.framework.pages;

import com.rishabh.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private final By deliveryAddress = By.id("address_delivery");
    private final By invoiceAddress = By.id("address_invoice");
    private final By orderCommentBox = By.cssSelector("textarea[name='message']");
    private final By placeOrderLink = By.cssSelector("a[href='/payment']");

    // shown instead of the checkout page itself when cart is opened while logged out
    private final By registerLoginModalLink = By.cssSelector(".modal-body a[href='/login']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(deliveryAddress) && isDisplayed(invoiceAddress);
    }

    public String getDeliveryAddressText() {
        return getText(deliveryAddress);
    }

    public String getInvoiceAddressText() {
        return getText(invoiceAddress);
    }

    public void enterOrderComment(String comment) {
        type(orderCommentBox, comment);
    }

    public void placeOrder() {
        click(placeOrderLink);
    }

    public boolean isRegisterLoginPromptVisible() {
        return isDisplayedQuick(registerLoginModalLink, 5);
    }

    public void goToLoginFromPrompt() {
        click(registerLoginModalLink);
    }
}
