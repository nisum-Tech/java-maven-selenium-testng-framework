package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(ConfigReader.get("username"), ConfigReader.get("password"));

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed());
    }

    @Test
    public void invalidLoginTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("invaliduser", "invalidpass");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed after invalid login.");
        Assert.assertTrue(loginPage.getErrorMessageText().contains("Your username is invalid!"), "Error message content is not as expected.");
    }
}