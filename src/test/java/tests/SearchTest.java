package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchResultsPage;

public class SearchTest extends BaseTest {

    @Test(description = "Test to verify that a product search returns results.")
    public void validProductSearchTest() {
        HomePage homePage = new HomePage(driver);
        SearchResultsPage searchResultsPage = homePage.searchForProduct("dress");

        Assert.assertTrue(searchResultsPage.getSearchResultCount() > 0, "Search for 'dress' should return results.");
        // Example of a more specific assertion, assuming 'Printed Dress' is an expected result.
        // Assert.assertTrue(searchResultsPage.isProductDisplayed("Printed Dress"), "'Printed Dress' was not found in search results.");
    }
}
