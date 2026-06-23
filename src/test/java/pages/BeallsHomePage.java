package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BeallsHomePage extends BasePage {

    @FindBy(id = "autocomplete-input")
    private WebElement searchInput;

    public BeallsHomePage(WebDriver driver) {
        super(driver);
    }

    public void searchForProduct(String productName) {
        searchInput.sendKeys(productName);
        searchInput.sendKeys(Keys.ENTER);
    }
}
