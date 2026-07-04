package com.rishabh.framework.pages;

import com.rishabh.framework.base.BasePage;
import com.rishabh.framework.components.FooterComponent;
import com.rishabh.framework.components.HeaderComponent;
import com.rishabh.framework.components.ProductGridComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private final HeaderComponent header;
    private final FooterComponent footer;
    private final ProductGridComponent productGrid;

    private final By logo = By.cssSelector(".logo.pull-left img");
    private final By featuresItemsHeading = By.xpath("//h2[text()='Features Items']");
    private final By categorySidebar = By.id("accordian");
    private final By womenCategoryToggle = By.xpath("//a[@href='#Women']");
    private final By brandsSidebar = By.className("brands-name");
    private final By recommendedItemsSection = By.id("recommended-item-carousel");

    public HomePage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
        this.footer = new FooterComponent(driver);
        this.productGrid = new ProductGridComponent(driver);
    }

    public void open(String baseUrl) {
        navigateTo(baseUrl);
    }

    public boolean isLoaded() {
        return isDisplayed(logo) && isDisplayed(featuresItemsHeading);
    }

    public HeaderComponent header() {
        return header;
    }

    public FooterComponent footer() {
        return footer;
    }

    public ProductGridComponent productGrid() {
        return productGrid;
    }

    public boolean isCategorySidebarVisible() {
        return isDisplayed(categorySidebar);
    }

    public boolean isBrandsSidebarVisible() {
        return isDisplayed(brandsSidebar);
    }

    public void expandCategory(String categoryName) {
        click(By.xpath("//a[@href='#" + categoryName + "']"));
    }

    public void selectSubCategory(String categoryName, String subCategoryText) {
        click(By.xpath("//div[@id='" + categoryName + "']//a[text()='" + subCategoryText + "']"));
    }

    public void selectBrand(String brandName) {
        click(By.cssSelector("a[href='/brand_products/" + brandName + "']"));
    }

    public boolean isRecommendedItemsVisible() {
        scrollTo(recommendedItemsSection);
        return isDisplayed(recommendedItemsSection);
    }
}
