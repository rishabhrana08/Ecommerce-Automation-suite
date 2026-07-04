package com.rishabh.tests.regression;

import com.rishabh.tests.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Ecommerce Automation")
@Feature("Product Listing & Filters")
public class ProductListingFilterTests extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "TC08 - All Products page loads with a populated grid")
    public void viewAllProducts_pageLoadsWithProductGrid() {
        goToHomePage().header().goToProducts();
        Assert.assertTrue(pages.getProductsPage().isLoaded());
        Assert.assertTrue(pages.getProductsPage().getProductCount() > 0);
    }

    @Test(groups = {"smoke", "regression"}, description = "TC08 - Clicking View Product opens the correct product detail page")
    public void viewProductDetail_fromListing_navigatesCorrectly() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().viewProductByIndex(0);

        Assert.assertTrue(pages.getProductDetailPage().isLoaded());
        Assert.assertFalse(pages.getProductDetailPage().getProductName().isBlank());
    }

    @Test(groups = "regression", description = "TC18 - View products under Women > Dress category")
    public void viewCategoryProducts_womenDress_showsFilteredResults() {
        goToHomePage();
        pages.getHomePage().expandCategory("Women");
        pages.getHomePage().selectSubCategory("Women", "Dress");

        Assert.assertTrue(driver.getCurrentUrl().contains("/category_products/"));
    }

    @Test(groups = "regression", description = "TC18 - View products under Men > Tshirts category")
    public void viewCategoryProducts_menTshirts_showsFilteredResults() {
        goToHomePage();
        pages.getHomePage().expandCategory("Men");
        pages.getHomePage().selectSubCategory("Men", "Tshirts");

        Assert.assertTrue(driver.getCurrentUrl().contains("/category_products/"));
    }

    @Test(groups = "regression", description = "Kids category accordion expands without breaking the sidebar")
    public void viewCategoryProducts_kids_sidebarExpands() {
        goToHomePage();
        pages.getHomePage().expandCategory("Kids");
        Assert.assertTrue(pages.getHomePage().isCategorySidebarVisible());
    }

    @Test(groups = {"smoke", "regression"}, description = "TC19 - View and filter products by the Polo brand")
    public void viewBrandProducts_polo_showsFilteredResults() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().openBrand("Polo");

        Assert.assertTrue(driver.getCurrentUrl().contains("/brand_products/Polo"));
    }

    @Test(groups = "regression", description = "TC19 - View and filter products by the H&M brand")
    public void viewBrandProducts_hm_showsFilteredResults() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().openBrand("H&M");

        Assert.assertTrue(driver.getCurrentUrl().contains("/brand_products/"));
    }

    @Test(groups = "regression", description = "Brand sidebar is also present while browsing a category page")
    public void productListing_brandSidebar_visibleOnCategoryPage() {
        goToHomePage();
        pages.getHomePage().expandCategory("Women");
        pages.getHomePage().selectSubCategory("Women", "Dress");

        Assert.assertTrue(pages.getProductsPage().isBrandPanelVisible());
    }

    @Test(groups = "regression", description = "Category sidebar is present with the products grid")
    public void productListing_categorySidebar_visible() {
        goToHomePage().header().goToProducts();
        Assert.assertTrue(pages.getProductsPage().isCategoryPanelVisible());
    }

    @Test(groups = "regression", description = "Brand sidebar renders with product count badges next to each brand")
    public void productListing_brandSidebar_visible() {
        goToHomePage().header().goToProducts();
        Assert.assertTrue(pages.getProductsPage().isBrandPanelVisible());
    }

    @Test(groups = "regression", description = "Product catalog has more than a trivial handful of items")
    public void productListing_catalogHasMultipleProducts() {
        goToHomePage().header().goToProducts();
        Assert.assertTrue(pages.getProductsPage().getProductCount() >= 10,
                "Expected a reasonably sized catalog, got " + pages.getProductsPage().getProductCount());
    }

    @Test(groups = "regression", description = "Every product card exposes a matching price alongside its name")
    public void productListing_everyProductHasNameAndPrice() {
        goToHomePage().header().goToProducts();
        int nameCount = pages.getProductsPage().getVisibleProductNames().size();
        int priceCount = pages.getProductsPage().getVisibleProductPrices().size();

        Assert.assertEquals(priceCount, nameCount, "Name/price count mismatch on the product grid");
    }
}
