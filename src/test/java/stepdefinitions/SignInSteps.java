// MISSING DEPENDENCY: io.cucumber:cucumber-java
package stepdefinitions;

import org.testng.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.DriverManager;

public class SignInSteps {

    @Given("user is on the login page")
    public void user_is_on_the_login_page() {
        // The @Before hook in DriverHooks navigates to the URL.
        // We can add an assertion here to ensure we are on the login page.
        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("login"), "User is not on the login page.");
    }

    @When("user enters valid username and password")
    public void user_enters_valid_username_and_password() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(ConfigReader.get("username"), ConfigReader.get("password"));
    }

    @And("clicks on the login button")
    public void clicks_on_the_login_button() {
        // This action is included in the `login` method called in the previous step.
        // This step is kept to match the feature file's structure.
    }

    @Then("user is redirected to the home page")
    public void user_is_redirected_to_the_home_page() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        Assert.assertTrue(homePage.isHomePageDisplayed(), "User is not redirected to the home page.");
    }
}
