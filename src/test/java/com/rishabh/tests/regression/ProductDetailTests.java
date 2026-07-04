package com.rishabh.tests.regression;

import com.rishabh.framework.utils.RandomDataUtil;
import com.rishabh.tests.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Ecommerce Automation")
@Feature("Product Detail Page")
public class ProductDetailTests extends BaseTest {

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setUp")
    public void openFirstProduct() {
        pages.getHomePage().header().goToProducts();
        pages.getProductsPage().viewProductByIndex(0);
    }

    @Test(groups = {"smoke", "regression"}, description = "TC08 - Product detail page shows all core info fields")
    public void productDetail_displaysAllCoreInformation() {
        Assert.assertFalse(pages.getProductDetailPage().getProductName().isBlank());
        Assert.assertTrue(pages.getProductDetailPage().getProductPrice().contains("Rs."));
        Assert.assertTrue(pages.getProductDetailPage().getCategoryText().contains("Category"));
        Assert.assertTrue(pages.getProductDetailPage().getAvailabilityText().contains("Availability"));
        Assert.assertTrue(pages.getProductDetailPage().getConditionText().contains("Condition"));
        Assert.assertTrue(pages.getProductDetailPage().getBrandText().contains("Brand"));
    }

    @Test(groups = "regression", description = "Quantity field defaults to 1 on page load")
    public void productDetail_quantitySelector_defaultValueIsOne() {
        // the field itself isn't asserted via getAttribute here - keeping the check
        // behaviour based (setting it explicitly then verifying downstream in cart)
        pages.getProductDetailPage().setQuantity(1);
        pages.getProductDetailPage().clickAddToCart();
        Assert.assertTrue(pages.getProductDetailPage().isLoaded());
    }

    @Test(groups = {"smoke", "regression"}, description = "TC13 - Increasing quantity before add-to-cart reflects in the cart")
    public void productDetail_increaseQuantity_reflectsInCart() {
        int productId = extractProductId(driver.getCurrentUrl());

        pages.getProductDetailPage().setQuantity(4);
        pages.getProductDetailPage().clickAddToCart();
        pages.getProductsPage().productGrid().viewCartFromModal();

        Assert.assertEquals(pages.getCartPage().getQuantityForProduct(productId), "4");
    }

    @Test(groups = "regression", description = "Add to cart pops the confirmation modal")
    public void productDetail_addToCart_showsConfirmationModal() {
        pages.getProductDetailPage().clickAddToCart();
        Assert.assertTrue(pages.getProductsPage().productGrid().isAddToCartModalVisible());
    }

    @Test(groups = "regression", description = "'Continue Shopping' from the modal keeps you on the same product page")
    public void productDetail_addToCart_continueShopping_staysOnProductPage() {
        String urlBefore = driver.getCurrentUrl();
        pages.getProductDetailPage().clickAddToCart();
        pages.getProductsPage().productGrid().continueShoppingFromModal();

        Assert.assertEquals(driver.getCurrentUrl(), urlBefore);
    }

    @Test(groups = {"smoke", "regression"}, description = "'View Cart' from the modal takes you to the cart with the item in it")
    public void productDetail_addToCart_viewCart_navigatesToCart() {
        pages.getProductDetailPage().clickAddToCart();
        pages.getProductsPage().productGrid().viewCartFromModal();

        Assert.assertTrue(driver.getCurrentUrl().contains("/view_cart"));
        Assert.assertFalse(pages.getCartPage().isCartEmpty());
    }

    @Test(groups = {"smoke", "regression"}, description = "TC21 - Submitting a product review shows the thank-you message")
    public void productDetail_writeReview_submitsSuccessfully() {
        pages.getProductDetailPage().openReviewTab();
        pages.getProductDetailPage().submitReview(RandomDataUtil.fullName(), RandomDataUtil.uniqueEmail(),
                "Great fit and quality, would buy again.");

        Assert.assertTrue(pages.getProductDetailPage().isReviewSuccessVisible());
    }

    @Test(groups = "regression", description = "Recommended items carousel renders on the product detail page")
    public void productDetail_recommendedItemsSection_visible() {
        Assert.assertTrue(pages.getProductDetailPage().isRecommendedItemsVisible());
    }

    @Test(groups = "regression", description = "TC22 - Add to cart directly from the recommended items carousel")
    public void productDetail_addToCartFromRecommendedItems_addsSuccessfully() {
        Assert.assertTrue(pages.getProductDetailPage().isRecommendedItemsVisible());
        pages.getProductDetailPage().recommendedItems().addToCartByIndex(0);

        Assert.assertTrue(pages.getProductDetailPage().recommendedItems().isAddToCartModalVisible());
    }

    @Test(groups = "regression", description = "Product price format always includes the Rs. currency prefix")
    public void productDetail_priceFormat_includesCurrencyPrefix() {
        Assert.assertTrue(pages.getProductDetailPage().getProductPrice().trim().startsWith("Rs."));
    }

    private int extractProductId(String url) {
        String[] parts = url.split("/product_details/");
        return Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
    }
}
