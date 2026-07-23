package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;

public class SearchTest extends BaseTest {

    @Test
    public void searchFunctionalityTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(ConfigReader.get("username"), ConfigReader.get("password"));

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "User should be on the home page after login.");

        homePage.search("sees");

        Assert.assertTrue(homePage.isSearchResultsDisplayed(), "Search results should be displayed.");
    }
}
