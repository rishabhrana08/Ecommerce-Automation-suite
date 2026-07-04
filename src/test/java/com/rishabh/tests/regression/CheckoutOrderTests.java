package com.rishabh.tests.regression;

import com.rishabh.framework.testdata.PaymentData;
import com.rishabh.framework.testdata.UserData;
import com.rishabh.tests.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Ecommerce Automation")
@Feature("Checkout & Orders")
public class CheckoutOrderTests extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "TC14 - Add to cart while logged out, register mid-checkout, then place the order")
    public void checkout_registerDuringCheckout_orderPlacedSuccessfully() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();
        pages.getCartPage().proceedToCheckout();

        Assert.assertTrue(pages.getCheckoutPage().isRegisterLoginPromptVisible());
        pages.getCheckoutPage().goToLoginFromPrompt();

        UserData user = UserData.builder().build();
        pages.getLoginPage().signup(user.getName(), user.getEmail());
        pages.getSignupPage().fillAccountInformation(user);
        pages.getSignupPage().submitAccountCreation();
        pages.getSignupPage().clickContinue();

        goToHomePage().header().goToCart();
        pages.getCartPage().proceedToCheckout();
        Assert.assertTrue(pages.getCheckoutPage().isLoaded());

        placeOrderAndPay();
        Assert.assertTrue(pages.getPaymentPage().isOrderPlacedSuccessVisible());
    }

    @Test(groups = "regression", description = "TC15 - Register first, then shop and check out as that user")
    public void checkout_registerBeforeCheckout_orderPlacedSuccessfully() {
        registerAndLoginNewUser();

        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();
        pages.getCartPage().proceedToCheckout();

        placeOrderAndPay();
        Assert.assertTrue(pages.getPaymentPage().isOrderPlacedSuccessVisible());
    }

    @Test(groups = {"smoke", "regression"}, description = "TC16 - Login with an existing account, then check out")
    public void checkout_loginBeforeCheckout_orderPlacedSuccessfully() {
        UserData user = registerAndLoginNewUser();
        pages.getHomePage().header().logout();
        loginExistingUser(user.getEmail(), user.getPassword());

        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();
        pages.getCartPage().proceedToCheckout();

        placeOrderAndPay();
        Assert.assertTrue(pages.getPaymentPage().isOrderPlacedSuccessVisible());
    }

    @Test(groups = "regression", description = "TC23 - Delivery/invoice address on checkout reflects the registered address")
    public void checkout_addressDetails_matchRegisteredAddress() {
        UserData user = registerAndLoginNewUser();

        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();
        pages.getCartPage().proceedToCheckout();

        Assert.assertTrue(pages.getCheckoutPage().getDeliveryAddressText().contains(user.getFirstName()));
        Assert.assertTrue(pages.getCheckoutPage().getInvoiceAddressText().contains(user.getFirstName()));
    }

    @Test(groups = "regression", description = "An order comment left at checkout doesn't block placing the order")
    public void checkout_orderComment_doesNotBlockOrder() {
        registerAndLoginNewUser();

        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();
        pages.getCartPage().proceedToCheckout();

        pages.getCheckoutPage().enterOrderComment("Please deliver in the evening, thanks!");
        pages.getCheckoutPage().placeOrder();

        Assert.assertTrue(pages.getPaymentPage().isLoaded());
    }

    @Test(groups = {"smoke", "regression"}, description = "TC24 - Invoice can be downloaded once the order is placed")
    public void checkout_downloadInvoice_afterPurchase() {
        registerAndLoginNewUser();

        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();
        pages.getCartPage().proceedToCheckout();

        placeOrderAndPay();
        Assert.assertTrue(pages.getPaymentPage().isOrderPlacedSuccessVisible());

        // just confirming the link is clickable without erroring - actual file download
        // verification would need a browser download-dir hook, out of scope here
        pages.getPaymentPage().downloadInvoice();
    }

    @Test(groups = "regression", description = "Checkout Place Order button lands on the payment page")
    public void checkout_placeOrder_navigatesToPaymentPage() {
        registerAndLoginNewUser();

        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();
        pages.getCartPage().proceedToCheckout();
        pages.getCheckoutPage().placeOrder();

        Assert.assertTrue(pages.getPaymentPage().isLoaded());
    }

    @Test(groups = "regression", description = "Continuing after a successful order returns to the home page")
    public void checkout_continueAfterOrder_returnsHome() {
        registerAndLoginNewUser();

        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();
        pages.getCartPage().proceedToCheckout();

        placeOrderAndPay();
        pages.getPaymentPage().clickContinue();

        Assert.assertTrue(pages.getHomePage().isLoaded());
    }

    @Test(groups = "regression", description = "Payment confirmation heading reads 'Order Placed!' after a successful payment")
    public void checkout_paymentSuccess_headingTextIsCorrect() {
        registerAndLoginNewUser();

        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();
        pages.getCartPage().proceedToCheckout();

        placeOrderAndPay();
        Assert.assertTrue(pages.getPaymentPage().isOrderPlacedSuccessVisible());
    }

    private void placeOrderAndPay() {
        pages.getCheckoutPage().placeOrder();
        pages.getPaymentPage().enterPaymentDetails(PaymentData.builder().build());
        pages.getPaymentPage().confirmPayment();
    }
}
