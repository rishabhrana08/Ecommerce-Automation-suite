package com.rishabh.framework.components;

import com.rishabh.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * The product grid (image + name + price + "Add to cart" + "View Product") shows
 * up identically on the home page, /products, category pages and brand pages, so
 * pulling it out here instead of copy-pasting the same locators four times.
 */
public class ProductGridComponent extends BasePage {

    private final By productCards = By.cssSelector(".product-image-wrapper");

    // every card renders "Add to cart" twice - once always visible under .productinfo,
    // and again inside .product-overlay which only shows up on :hover. Scoping to
    // .productinfo here so we don't end up waiting on the hidden hover copy.
    private final By addToCartBtn = By.cssSelector(".productinfo a.add-to-cart");
    private final By viewProductLinks = By.cssSelector(".choose a[href^='/product_details/']");

    private final By cartModal = By.id("cartModal");
    private final By continueShoppingBtn = By.cssSelector(".close-modal.btn-block");
    private final By viewCartLinkInModal = By.cssSelector("#cartModal a[href='/view_cart']");

    public ProductGridComponent(WebDriver driver) {
        super(driver);
    }

    public int getVisibleProductCount() {
        return allVisible(productCards).size();
    }

    public void addToCartByProductId(int productId) {
        By locator = By.cssSelector("a.add-to-cart[data-product-id='" + productId + "']");
        scrollTo(locator);
        click(locator);
    }

    /**
     * The listing page renders two "Add to cart" buttons per card (one hidden overlay,
     * one under productinfo) - index here is just the nth visible one on the page.
     */
    public void addToCartByIndex(int zeroBasedIndex) {
        List<WebElement> buttons = allVisible(addToCartBtn);
        click(buttons.get(zeroBasedIndex));
    }

    public void clickViewProductByIndex(int zeroBasedIndex) {
        List<WebElement> links = allVisible(viewProductLinks);
        click(links.get(zeroBasedIndex));
    }

    public boolean isAddToCartModalVisible() {
        return isDisplayed(cartModal);
    }

    public void continueShoppingFromModal() {
        click(continueShoppingBtn);
    }

    public void viewCartFromModal() {
        click(viewCartLinkInModal);
    }
}
