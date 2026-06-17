package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Set;

public class HomePage extends BasePage {

    @FindBy(linkText = "Elemental Selenium")
    private WebElement elementalSeleniumLink;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isHomePageDisplayed() {
        return driver.getCurrentUrl().contains("secure");
    }

    public String clickElementalSeleniumLinkAndGetNewTabTitle() {
        String originalWindow = driver.getWindowHandle();
        elementalSeleniumLink.click();

        Set<String> allWindows = driver.getWindowHandles();
        String newWindowTitle = "";
        String newWindowHandle = null;

        for (String windowHandle : allWindows) {
            if (!originalWindow.equals(windowHandle)) {
                newWindowHandle = windowHandle;
                break;
            }
        }

        if (newWindowHandle != null) {
            driver.switchTo().window(newWindowHandle);
            newWindowTitle = driver.getTitle();
            driver.switchTo().window(originalWindow);
        }

        return newWindowTitle;
    }
}