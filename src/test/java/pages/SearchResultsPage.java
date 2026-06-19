package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class SearchResultsPage extends BasePage {

    @FindBy(id = "search_results") // FIXME: Replace with actual locator for the results container
    private WebElement searchResultsContainer;

    @FindBy(className = "product-title") // FIXME: Replace with actual locator for product titles
    private List<WebElement> searchResults;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public int getSearchResultCount() {
        return searchResults.size();
    }

    public boolean isProductDisplayed(String productName) {
        for (WebElement result : searchResults) {
            if (result.getText().equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
    }
}
