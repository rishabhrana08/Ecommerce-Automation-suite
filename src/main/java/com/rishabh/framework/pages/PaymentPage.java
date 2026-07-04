package com.rishabh.framework.pages;

import com.rishabh.framework.base.BasePage;
import com.rishabh.framework.testdata.PaymentData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentPage extends BasePage {

    private final By nameOnCard = By.cssSelector("[name='name_on_card']");
    private final By cardNumber = By.cssSelector("[name='card_number']");
    private final By cvc = By.cssSelector("[name='cvc']");
    private final By expiryMonth = By.cssSelector("[name='expiry_month']");
    private final By expiryYear = By.cssSelector("[name='expiry_year']");
    private final By payButton = By.id("submit");

    private final By orderPlacedHeading = By.cssSelector("[data-qa='order-placed']");
    private final By downloadInvoiceLink = By.cssSelector(".btn.btn-default.check_trigger, a[href^='/download_invoice']");
    private final By continueButton = By.cssSelector("[data-qa='continue-button']");

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(nameOnCard);
    }

    public void enterPaymentDetails(PaymentData payment) {
        type(nameOnCard, payment.getNameOnCard());
        type(cardNumber, payment.getCardNumber());
        type(cvc, payment.getCvc());
        type(expiryMonth, payment.getExpiryMonth());
        type(expiryYear, payment.getExpiryYear());
    }

    public void confirmPayment() {
        click(payButton);
    }

    public boolean isOrderPlacedSuccessVisible() {
        return isDisplayed(orderPlacedHeading);
    }

    public void downloadInvoice() {
        click(downloadInvoiceLink);
    }

    public void clickContinue() {
        click(continueButton);
    }
}
