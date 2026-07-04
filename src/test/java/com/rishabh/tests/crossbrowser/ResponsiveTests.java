package com.rishabh.tests.crossbrowser;

import com.rishabh.framework.enums.ViewportType;
import com.rishabh.framework.utils.RandomDataUtil;
import com.rishabh.tests.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.openqa.selenium.Dimension;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * These resize the same driver mid-test rather than relying only on the suite-level
 * browser/viewport parameters, so responsive behaviour gets checked at the individual
 * test level too (desktop -> laptop -> tablet -> mobile breakpoints).
 */
@Epic("Ecommerce Automation")
@Feature("Cross-Browser & Responsive")
public class ResponsiveTests extends BaseTest {

    @DataProvider(name = "viewports")
    public Object[][] viewports() {
        return new Object[][]{
                {ViewportType.DESKTOP},
                {ViewportType.LAPTOP},
                {ViewportType.TABLET},
                {ViewportType.MOBILE}
        };
    }

    private void resizeTo(ViewportType viewport) {
        driver.manage().window().setSize(new Dimension(viewport.getWidth(), viewport.getHeight()));
    }

    @Test(dataProvider = "viewports", groups = {"smoke", "regression"},
            description = "Home page renders its core elements at every breakpoint")
    public void homePage_rendersCorrectly_acrossViewports(ViewportType viewport) {
        resizeTo(viewport);
        goToHomePage();
        Assert.assertTrue(pages.getHomePage().isLoaded(), "Home page failed to load at " + viewport);
    }

    @Test(dataProvider = "viewports", groups = "regression",
            description = "Products grid still renders products at every breakpoint")
    public void productsPage_gridRendersProducts_acrossViewports(ViewportType viewport) {
        resizeTo(viewport);
        goToHomePage().header().goToProducts();
        Assert.assertTrue(pages.getProductsPage().getProductCount() > 0, "No products rendered at " + viewport);
    }

    @Test(dataProvider = "viewports", groups = "regression",
            description = "Cart icon in the header stays reachable at every breakpoint")
    public void header_cartIcon_accessibleAcrossViewports(ViewportType viewport) {
        resizeTo(viewport);
        goToHomePage();
        Assert.assertTrue(pages.getHomePage().header().isCartIconVisible(), "Cart icon not visible at " + viewport);
    }

    @Test(groups = {"smoke", "regression"}, description = "Add to cart and view cart works end to end on a mobile viewport")
    public void cartFlow_addAndView_worksOnMobileViewport() {
        resizeTo(ViewportType.MOBILE);
        goToHomePage().header().goToProducts();
        pages.getProductsPage().productGrid().addToCartByIndex(0);
        pages.getProductsPage().productGrid().viewCartFromModal();

        Assert.assertFalse(pages.getCartPage().isCartEmpty());
    }

    @Test(groups = "regression", description = "Add to cart from the product detail page works on a tablet viewport")
    public void productDetail_addToCart_worksOnTabletViewport() {
        resizeTo(ViewportType.TABLET);
        goToHomePage().header().goToProducts();
        pages.getProductsPage().viewProductByIndex(0);
        pages.getProductDetailPage().clickAddToCart();

        Assert.assertTrue(pages.getProductsPage().productGrid().isAddToCartModalVisible());
    }

    @Test(groups = "regression", description = "Login form fields remain usable on a mobile viewport")
    public void loginPage_formUsable_onMobileViewport() {
        resizeTo(ViewportType.MOBILE);
        goToHomePage().header().goToSignupLogin();
        Assert.assertTrue(pages.getLoginPage().isLoaded());
    }

    @Test(groups = "regression", description = "Newsletter subscription still works when squeezed into a mobile layout")
    public void footerSubscription_worksOnMobileViewport() {
        resizeTo(ViewportType.MOBILE);
        goToHomePage().footer().subscribe(RandomDataUtil.uniqueEmail());
        Assert.assertTrue(pages.getHomePage().footer().isSubscribeSuccessVisible());
    }

    @Test(dataProvider = "viewports", groups = "regression",
            description = "Search functionality is usable at every breakpoint")
    public void productSearch_worksAcrossViewports(ViewportType viewport) {
        resizeTo(viewport);
        goToHomePage().header().goToProducts();
        pages.getProductsPage().searchProduct("Top");

        Assert.assertTrue(pages.getProductsPage().isSearchedProductsHeadingVisible(), "Search failed at " + viewport);
    }
}
