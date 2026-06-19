package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HomePage extends BasePage {

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isHomePageDisplayed() {
        return driver.getCurrentUrl().contains("inventory.html");
    }

    public ProductPage selectProductByName(String productName) {
        for (WebElement item : inventoryItems) {
            WebElement itemNameElement = item.findElement(By.className("inventory_item_name"));
            if (itemNameElement.getText().equals(productName)) {
                itemNameElement.click();
                return new ProductPage(driver);
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }
}
