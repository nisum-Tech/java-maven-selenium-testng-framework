package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductPage extends BasePage {

    @FindBy(css = "button.btn_inventory")
    private WebElement addToCartButton;

    @FindBy(id = "shopping_cart_container")
    private WebElement cartLink;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public void addToCart() {
        addToCartButton.click();
    }

    public CartPage goToCart() {
        cartLink.click();
        return new CartPage(driver);
    }
}
