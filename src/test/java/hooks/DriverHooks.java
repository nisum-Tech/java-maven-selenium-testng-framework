// MISSING DEPENDENCY: io.cucumber:cucumber-java
package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import utils.ConfigReader;
import utils.DriverManager;

public class DriverHooks {

    @Before
    public void setupDriver() {
        DriverManager.getDriver().get(ConfigReader.get("url"));
    }

    @After
    public void tearDownDriver() {
        DriverManager.quitDriver();
    }
}
