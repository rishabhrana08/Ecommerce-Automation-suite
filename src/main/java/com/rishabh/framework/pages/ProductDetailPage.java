package com.rishabh.framework.pages;

import com.rishabh.framework.base.BasePage;
import com.rishabh.framework.components.ProductGridComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailPage extends BasePage {

    private final ProductGridComponent recommendedItemsGrid;

    private final By productName = By.cssSelector(".product-information h2");
    private final By productPrice = By.cssSelector(".product-information span span");
    private final By productCategory = By.xpath("//div[@class='product-information']/p[contains(text(),'Category')]");
    private final By productAvailability = By.xpath("//div[@class='product-information']//p[contains(.,'Availability')]");
    private final By productCondition = By.xpath("//div[@class='product-information']//p[contains(.,'Condition')]");
    private final By productBrand = By.xpath("//div[@class='product-information']//p[contains(.,'Brand')]");

    private final By quantityInput = By.id("quantity");
    private final By addToCartButton = By.cssSelector(".cart");

    private final By reviewTab = By.cssSelector("a[href='#reviews']");
    private final By reviewNameInput = By.id("name");
    private final By reviewEmailInput = By.id("email");
    private final By reviewMessageInput = By.id("review");
    private final By reviewSubmitButton = By.id("button-review");
    private final By reviewSuccessMsg = By.xpath("//span[contains(text(),'Thank you for your review')]");

    private final By recommendedItemsSection = By.id("recommended-item-carousel");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
        this.recommendedItemsGrid = new ProductGridComponent(driver);
    }

    public ProductGridComponent recommendedItems() {
        return recommendedItemsGrid;
    }

    public boolean isLoaded() {
        return isDisplayed(productName) && isDisplayed(addToCartButton);
    }

    public String getProductName() {
        return getText(productName);
    }

    public String getProductPrice() {
        return getText(productPrice);
    }

    public String getCategoryText() {
        return getText(productCategory);
    }

    public String getAvailabilityText() {
        return getText(productAvailability);
    }

    public String getConditionText() {
        return getText(productCondition);
    }

    public String getBrandText() {
        return getText(productBrand);
    }

    public void setQuantity(int qty) {
        type(quantityInput, String.valueOf(qty));
    }

    public void clickAddToCart() {
        click(addToCartButton);
    }

    public void openReviewTab() {
        click(reviewTab);
    }

    public void submitReview(String name, String email, String message) {
        type(reviewNameInput, name);
        type(reviewEmailInput, email);
        type(reviewMessageInput, message);
        click(reviewSubmitButton);
    }

    public boolean isReviewSuccessVisible() {
        return isDisplayed(reviewSuccessMsg);
    }

    public boolean isRecommendedItemsVisible() {
        scrollTo(recommendedItemsSection);
        return isDisplayed(recommendedItemsSection);
    }
}
