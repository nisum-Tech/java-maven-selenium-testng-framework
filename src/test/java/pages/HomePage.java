package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    @FindBy(id = "search") // FIXME: Replace with actual locator for search input
    private WebElement searchInput;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isHomePageDisplayed() {
        return driver.getCurrentUrl().contains("secure");
    }

    public SearchResultsPage searchForProduct(String productName) {
        searchInput.sendKeys(productName);
        searchInput.sendKeys(Keys.ENTER);
        return new SearchResultsPage(driver);
    }
}
