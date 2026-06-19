package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Optional;

public class CartPage extends BasePage {

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isItemInCart(String expectedItemName) {
        return findCartItemByName(expectedItemName).isPresent();
    }

    public int getItemsCount() {
        if (cartItems == null) {
            return 0;
        }
        return cartItems.size();
    }

    private Optional<WebElement> findCartItemByName(String itemName) {
        return cartItems.stream()
                .filter(item -> item.findElement(By.className("inventory_item_name")).getText().equals(itemName))
                .findFirst();
    }
}
