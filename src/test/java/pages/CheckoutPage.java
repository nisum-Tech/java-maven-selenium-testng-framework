package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BasePage {

    @FindBy(xpath = "//*[@id='content']/h1")
    private WebElement secureCheckoutHeading;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public String getSecureCheckoutHeading() {
        return getPageHeading(secureCheckoutHeading);
    }
}
