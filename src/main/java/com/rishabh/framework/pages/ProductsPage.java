package com.rishabh.framework.pages;

import com.rishabh.framework.base.BasePage;
import com.rishabh.framework.components.ProductGridComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class ProductsPage extends BasePage {

    private final ProductGridComponent productGrid;

    private final By pageHeading = By.xpath("//h2[text()='All Products']");
    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By searchedProductsHeading = By.xpath("//h2[text()='Searched Products']");
    private final By productNames = By.cssSelector(".productinfo p");
    private final By productPrices = By.cssSelector(".productinfo h2");

    private final By categoryPanel = By.className("category-products");
    private final By brandPanel = By.className("brands-name");

    public ProductsPage(WebDriver driver) {
        super(driver);
        this.productGrid = new ProductGridComponent(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(pageHeading);
    }

    public ProductGridComponent productGrid() {
        return productGrid;
    }

    public void searchProduct(String keyword) {
        type(searchInput, keyword);
        click(searchButton);
    }

    public boolean isSearchedProductsHeadingVisible() {
        return isDisplayed(searchedProductsHeading);
    }

    public List<String> getVisibleProductNames() {
        return allVisible(productNames).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public List<String> getVisibleProductPrices() {
        return allVisible(productPrices).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public int getProductCount() {
        return allVisible(productNames).size();
    }

    public boolean isCategoryPanelVisible() {
        return isDisplayed(categoryPanel);
    }

    public boolean isBrandPanelVisible() {
        return isDisplayed(brandPanel);
    }

    public void openCategory(String categoryHeader) {
        click(By.xpath("//a[@href='#" + categoryHeader + "']"));
    }

    public void openSubCategory(String categoryHeader, String subCategoryText) {
        click(By.xpath("//div[@id='" + categoryHeader + "']//a[text()='" + subCategoryText + "']"));
    }

    public void openBrand(String brandName) {
        // brand links render as "<span>(6)</span>Polo" so an exact linkText match won't
        // hit - the href is stable and doesn't care about that count badge text
        click(By.cssSelector("a[href='/brand_products/" + brandName + "']"));
    }

    public void viewProductByIndex(int zeroBasedIndex) {
        productGrid.clickViewProductByIndex(zeroBasedIndex);
    }
}
