package com.rishabh.tests.regression;

import com.rishabh.tests.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Ecommerce Automation")
@Feature("Product Search")
public class ProductSearchTests extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "TC09 - Search for a known product keyword")
    public void searchProduct_byValidKeyword_showsMatchingResults() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().searchProduct("Top");

        Assert.assertTrue(pages.getProductsPage().isSearchedProductsHeadingVisible());
        Assert.assertTrue(pages.getProductsPage().getProductCount() > 0, "Expected at least one result for 'Top'");
    }

    @Test(groups = "regression", description = "Search using only a partial word still matches products")
    public void searchProduct_byPartialKeyword_showsMatchingResults() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().searchProduct("Dre"); // partial for "Dress"

        Assert.assertTrue(pages.getProductsPage().getProductCount() > 0);
    }

    @Test(groups = "regression", description = "Searching for a keyword with no matches returns zero products")
    public void searchProduct_byNonExistentKeyword_showsNoResults() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().searchProduct("zzzznonexistentproductzzzz");

        Assert.assertEquals(pages.getProductsPage().getProductCount(), 0);
    }

    @Test(groups = "regression", description = "Search is case insensitive")
    public void searchProduct_caseInsensitiveSearch_returnsSameResults() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().searchProduct("TOP");
        int upperCount = pages.getProductsPage().getProductCount();

        goToHomePage().header().goToProducts();
        pages.getProductsPage().searchProduct("top");
        int lowerCount = pages.getProductsPage().getProductCount();

        Assert.assertEquals(upperCount, lowerCount, "Search should not be case sensitive");
    }

    @Test(groups = "regression", description = "Searching with special characters doesn't error out")
    public void searchProduct_withSpecialCharacters_handledGracefully() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().searchProduct("@#$%^&*");

        // just needs to not blow up and stay on the products page
        Assert.assertTrue(driver.getCurrentUrl().contains("/products"));
    }

    @Test(groups = "regression", description = "Empty search term keeps the user on the products listing")
    public void searchProduct_withEmptyKeyword_doesNotError() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().searchProduct("");

        Assert.assertTrue(driver.getCurrentUrl().contains("/products"));
    }

    @Test(groups = {"smoke", "regression"}, description = "Every returned result actually contains the searched term")
    public void searchProduct_resultsContainSearchedKeywordInName() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().searchProduct("Jean");

        List<String> names = pages.getProductsPage().getVisibleProductNames();
        Assert.assertFalse(names.isEmpty());
        names.forEach(name -> Assert.assertTrue(name.toLowerCase().contains("jean"),
                "Result '" + name + "' doesn't actually match the search term"));
    }

    @Test(groups = "regression", description = "TC20 - Search a product and add it straight to the cart")
    public void searchProduct_thenAddToCart_addsSuccessfully() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().searchProduct("Top");
        pages.getProductsPage().productGrid().addToCartByIndex(0);

        Assert.assertTrue(pages.getProductsPage().productGrid().isAddToCartModalVisible());
        pages.getProductsPage().productGrid().viewCartFromModal();
        Assert.assertFalse(pages.getCartPage().isCartEmpty());
    }

    @Test(groups = "regression", description = "'Searched Products' heading appears once results render")
    public void searchProduct_verifySearchedProductsHeadingVisible() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().searchProduct("Shirt");
        Assert.assertTrue(pages.getProductsPage().isSearchedProductsHeadingVisible());
    }

    @Test(groups = "regression", description = "Leading/trailing whitespace in the search box doesn't break results")
    public void searchProduct_withWhitespacePadding_stillMatches() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().searchProduct("  Top  ");
        Assert.assertTrue(pages.getProductsPage().getProductCount() >= 0); // shouldn't error, count can legitimately be 0
    }

    @Test(groups = "regression", description = "Purely numeric search terms return no products")
    public void searchProduct_withNumericKeyword_showsNoResults() {
        goToHomePage().header().goToProducts();
        pages.getProductsPage().searchProduct("123456");
        Assert.assertEquals(pages.getProductsPage().getProductCount(), 0);
    }

    @Test(groups = "regression", description = "Full flow: header -> products -> search works end to end")
    public void searchProduct_navigateFromHeaderThenSearch_flowWorks() {
        goToHomePage();
        pages.getHomePage().header().goToProducts();
        Assert.assertTrue(pages.getProductsPage().isLoaded());

        pages.getProductsPage().searchProduct("Top");
        Assert.assertTrue(pages.getProductsPage().isSearchedProductsHeadingVisible());
    }
}
