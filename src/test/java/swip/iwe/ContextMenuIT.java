package swip.iwe;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import swip.junit.Config;
import swip.junit.SeleniumWebDriverRunner;

import javax.inject.Inject;

@RunWith(SeleniumWebDriverRunner.class)
@Config(exclude = {"browserName=safari", "browserName=htmlunit"})
public class ContextMenuIT {

    @Inject
    private WebDriver driver;

    @Test
    public void showContextMenu() throws Exception {
        driver.get("http://localhost:8080/context-menu.html");

        new Actions(driver)
                .contextClick(driver.findElement(By.id("hascontextmenu")))
                .build()
                .perform();
    }
}