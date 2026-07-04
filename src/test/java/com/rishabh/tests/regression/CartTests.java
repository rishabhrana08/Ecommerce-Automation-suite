package com.rishabh.tests.regression;

import com.rishabh.tests.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Ecommerce Automation")
@Feature("Shopping Cart")
public class CartTests extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "TC12 - Add a single product to the cart")
    public void cart_addSingleProduct_reflectsInCart() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();

        Assert.assertFalse(pages.getCartPage().isCartEmpty());
        Assert.assertEquals(pages.getCartPage().getLineItemCount(), 1);
    }

    @Test(groups = {"smoke", "regression"}, description = "Adding two different products puts both in the cart")
    public void cart_addMultipleProducts_allAppearInCart() {
        goToHomePage().header().goToProducts();

        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().continueShoppingFromModal();
        pages.getProductsPage().productGrid().addToCartByIndex(1);
        pages.getProductsPage().productGrid().viewCartFromModal();

        Assert.assertEquals(pages.getCartPage().getLineItemCount(), 2);
    }

    @Test(groups = {"smoke", "regression"}, description = "TC17 - Remove a product from the cart")
    public void cart_removeProduct_removedSuccessfully() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().viewProductByIndex(0);
        int productId = extractProductId(driver.getCurrentUrl());

        pages.getProductDetailPage().clickAddToCart();
        pages.getProductsPage().productGrid().viewCartFromModal();
        Assert.assertTrue(pages.getCartPage().isProductInCart(productId));

        pages.getCartPage().removeProduct(productId);
        Assert.assertFalse(pages.getCartPage().isProductInCart(productId));
    }

    @Test(groups = "regression", description = "Removing the only item in the cart shows the empty cart message")
    public void cart_removeOnlyProduct_showsEmptyCartMessage() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().viewProductByIndex(0);
        int productId = extractProductId(driver.getCurrentUrl());

        pages.getProductDetailPage().clickAddToCart();
        pages.getProductsPage().productGrid().viewCartFromModal();
        pages.getCartPage().removeProduct(productId);

        Assert.assertTrue(pages.getCartPage().isCartEmpty());
    }

    @Test(groups = "regression", description = "Navigating straight to an empty cart shows the empty state")
    public void cart_emptyCart_navigateDirectly_showsEmptyMessage() {
        goToHomePage().header().goToCart();
        Assert.assertTrue(pages.getCartPage().isCartEmpty());
    }

    @Test(groups = {"smoke", "regression"}, description = "Proceeding to checkout while logged out prompts a register/login modal")
    public void cart_proceedToCheckoutWhileLoggedOut_showsLoginPrompt() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();

        pages.getCartPage().proceedToCheckout();
        Assert.assertTrue(pages.getCheckoutPage().isRegisterLoginPromptVisible());
    }

    @Test(groups = {"smoke", "regression"}, description = "TC16 - Logged in user can proceed straight to the checkout page")
    public void cart_proceedToCheckoutWhileLoggedIn_navigatesToCheckoutPage() {
        registerAndLoginNewUser();

        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();

        pages.getCartPage().proceedToCheckout();
        Assert.assertTrue(pages.getCheckoutPage().isLoaded());
    }

    @Test(groups = "regression", description = "TC20 - Cart items added while logged out survive logging in")
    public void cart_addedWhileLoggedOut_persistsAfterLogin() {
        var user = registerAndLoginNewUser();
        pages.getHomePage().header().logout();

        goToHomePage().header().goToProducts();
        pages.getProductsPage().viewProductByIndex(0);
        int productId = extractProductId(driver.getCurrentUrl());
        pages.getProductDetailPage().clickAddToCart();
        pages.getProductsPage().productGrid().viewCartFromModal();

        goToHomePage().header().goToSignupLogin();
        pages.getLoginPage().login(user.getEmail(), user.getPassword());

        goToHomePage().header().goToCart();
        Assert.assertTrue(pages.getCartPage().isProductInCart(productId), "Cart contents should persist across login");
    }

    @Test(groups = "regression", description = "Products added from two different categories both land in the cart")
    public void cart_addFromDifferentCategories_bothVisible() {
        goToHomePage();
        pages.getHomePage().expandCategory("Women");
        pages.getHomePage().selectSubCategory("Women", "Dress");
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().continueShoppingFromModal();

        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();

        Assert.assertEquals(pages.getCartPage().getLineItemCount(), 2);
    }

    @Test(groups = "regression", description = "Cart quantity control is a disabled stepper, not a free-text field")
    public void cart_quantityControl_isReadOnlyStepper() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();

        boolean disabled = driver.findElement(By.cssSelector("td.cart_quantity button"))
                .getAttribute("disabled") != null;
        Assert.assertTrue(disabled, "Quantity field is expected to be a disabled display-only stepper on this site");
    }

    @Test(groups = "regression", description = "Line total for a product matches unit price times quantity")
    public void cart_lineTotal_matchesUnitPriceTimesQuantity() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().viewProductByIndex(0);
        int productId = extractProductId(driver.getCurrentUrl());
        String unitPriceText = pages.getProductDetailPage().getProductPrice();

        pages.getProductDetailPage().setQuantity(2);
        pages.getProductDetailPage().clickAddToCart();
        pages.getProductsPage().productGrid().viewCartFromModal();

        int unitPrice = Integer.parseInt(unitPriceText.replaceAll("[^0-9]", ""));
        int expectedTotal = unitPrice * 2;
        int actualTotal = Integer.parseInt(pages.getCartPage().getTotalPriceForProduct(productId).replaceAll("[^0-9]", ""));

        Assert.assertEquals(actualTotal, expectedTotal);
    }

    private int extractProductId(String url) {
        String[] parts = url.split("/product_details/");
        return Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
    }
}
