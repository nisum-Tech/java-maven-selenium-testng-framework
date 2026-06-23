package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyBagPage extends BasePage {

    @FindBy(xpath = "//*[@id='content']/div[3]/h1")
    private WebElement myBagHeading;

    @FindBy(xpath = "//*[@id='content']/div[3]/h1/span")
    private WebElement bagProductCount;

    @FindBy(xpath = "//*[@id='content']/div[3]/div[1]/div/div[3]/button[2]")
    private WebElement checkoutAsGuestButton;

    public MyBagPage(WebDriver driver) {
        super(driver);
    }

    public String getMyBagHeading() {
        return getPageHeading(myBagHeading);
    }

    public int getBagProductCount() {
        waitForElementVisibility(bagProductCount);
        String countText = bagProductCount.getText().replaceAll("[^0-9]", "").trim();
        return Integer.parseInt(countText);
    }

    public void clickCheckoutAsGuest() {
        clickElement(checkoutAsGuestButton);
    }
}
