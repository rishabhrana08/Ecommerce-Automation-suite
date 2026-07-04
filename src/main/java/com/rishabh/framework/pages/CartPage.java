package com.rishabh.framework.pages;

import com.rishabh.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private final By cartTable = By.id("cart_info");
    private final By cartRows = By.cssSelector("#cart_info_table tbody tr");
    private final By emptyCartMsg = By.id("empty_cart");
    private final By proceedToCheckoutBtn = By.cssSelector(".check_out");
    private final By quantityCells = By.cssSelector("td.cart_quantity button");
    private final By totalPriceCells = By.cssSelector("p.cart_total_price");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(cartTable);
    }

    public boolean isCartEmpty() {
        return isDisplayedQuick(emptyCartMsg, 5);
    }

    public int getLineItemCount() {
        return allVisible(cartRows).size();
    }

    public void removeProduct(int productId) {
        By deleteBtn = By.cssSelector("#product-" + productId + " .cart_quantity_delete");
        click(deleteBtn);
        // row removal happens via an ajax call, give it a moment to actually leave the DOM
        waitUtils.waitForInvisible(By.id("product-" + productId));
    }

    public String getQuantityForProduct(int productId) {
        return getText(By.cssSelector("#product-" + productId + " .cart_quantity button"));
    }

    public String getTotalPriceForProduct(int productId) {
        return getText(By.cssSelector("#product-" + productId + " .cart_total_price"));
    }

    public boolean isProductInCart(int productId) {
        return isDisplayedQuick(By.id("product-" + productId), 5);
    }

    public List<String> getAllLineTotals() {
        return allVisible(totalPriceCells).stream().map(WebElement::getText).toList();
    }

    public void proceedToCheckout() {
        click(proceedToCheckoutBtn);
    }
}
