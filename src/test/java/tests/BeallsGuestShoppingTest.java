package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BeallsHomePage;
import pages.CheckoutPage;
import pages.MyBagPage;
import pages.SearchResultsPage;

public class BeallsGuestShoppingTest extends BaseTest {

    @Test
    public void guestShoppingExperienceTest() {
        driver.get("https://www.bealls.com/");

        BeallsHomePage homePage = new BeallsHomePage(driver);
        homePage.searchForProduct("sandals");

        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        Assert.assertTrue(searchResultsPage.getSearchResultHeading().contains("Search results for"), "Search results heading is incorrect.");
        Assert.assertTrue(searchResultsPage.getResultsCount() > 0, "Search results count should be greater than 0.");

        searchResultsPage.addFirstProductToBag();
        searchResultsPage.waitForAddedToBagPopup();
        searchResultsPage.waitForAddedToBagPopupToDisappear();
        
        searchResultsPage.goToBag();

        MyBagPage myBagPage = new MyBagPage(driver);
        Assert.assertTrue(myBagPage.getMyBagHeading().contains("My Bag"), "My Bag heading is incorrect.");
        Assert.assertTrue(myBagPage.getBagProductCount() > 0, "Bag product count should be greater than 0.");
        
        myBagPage.clickCheckoutAsGuest();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        Assert.assertTrue(checkoutPage.getSecureCheckoutHeading().contains("Secure Checkout"), "Secure Checkout heading is incorrect.");
    }
}
