package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isHomePageDisplayed() {
        return driver.getCurrentUrl().contains("secure");
    }

    @FindBy(id = "search-input") // FIXME: Using placeholder locator
    private WebElement searchInput;

    @FindBy(id = "search-button") // FIXME: Using placeholder locator
    private WebElement searchButton;
    
    // Assuming search results are displayed in a container with this ID
    @FindBy(id = "search-results") // FIXME: Using placeholder locator
    private WebElement searchResultsContainer;

    public void search(String searchTerm) {
        searchInput.sendKeys(searchTerm);
        searchButton.click();
    }

    public boolean isSearchResultsDisplayed() {
        return searchResultsContainer.isDisplayed();
    }
}