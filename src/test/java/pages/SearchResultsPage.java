package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class SearchResultsPage extends BasePage {

    @FindBy(xpath = "//*[@id='content']/div[2]/h1")
    private WebElement searchResultHeading;

    @FindBy(xpath = "//*[@id='content']/div[2]/div/div[2]/div")
    private WebElement resultCount;

    @FindBy(xpath = ".//button[text()='Add to bag']")
    private List<WebElement> addToBagButtons;

    @FindBy(xpath = "//*[contains(text(),'Added')]")
    private WebElement addedToBagPopup;
    
    private final By addedToBagPopupBy = By.xpath("//*[contains(text(),'Added')]");

    @FindBy(xpath = "/html/body/header/nav/div/div/div[1]/div[2]/div/ul/li[3]/div/div/a")
    private WebElement bagIcon;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public String getSearchResultHeading() {
        return getPageHeading(searchResultHeading);
    }

    public int getResultsCount() {
        waitForElementVisibility(resultCount);
        String countText = resultCount.getText().replaceAll("[^0-9]", "").trim();
        if (countText.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(countText);
    }

    public void addFirstProductToBag() {
        wait.until(driver -> addToBagButtons.size() > 0);
        clickElement(addToBagButtons.get(0));
    }
    
    public void waitForAddedToBagPopup() {
        waitForElementVisibility(addedToBagPopup);
    }
    
    public void waitForAddedToBagPopupToDisappear() {
        waitForElementToDisappear(addedToBagPopupBy);
    }
    
    public void goToBag() {
        clickElement(bagIcon);
    }
}
