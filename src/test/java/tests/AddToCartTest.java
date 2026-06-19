package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductPage;
import utils.ConfigReader;

public class AddToCartTest extends BaseTest {

    @Test
    public void testAddToCartFunctionality() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(ConfigReader.get("username"), ConfigReader.get("password"));

        HomePage homePage = new HomePage(driver);
        ProductPage productPage = homePage.selectProductByName("Sauce Labs Backpack");

        productPage.addToCart();
        CartPage cartPage = productPage.goToCart();

        Assert.assertTrue(cartPage.isItemInCart("Sauce Labs Backpack"), "Item 'Sauce Labs Backpack' should be in the cart.");
        Assert.assertEquals(cartPage.getItemsCount(), 1, "Cart should contain 1 item.");
    }
}
