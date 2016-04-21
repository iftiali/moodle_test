package swip.ch13framework.v1_5;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.FluentWait;
import swip.ch13framework.v1.Element;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public interface ExplicitWait {

    Element findElement(By by); // <1>

    default Element untilFound(By by) {  //<2>
        return new FluentWait<>(this)
            .withTimeout(1, SECONDS)
            .pollingEvery(10, MILLISECONDS)
            .ignoring(NoSuchElementException.class)
            .until((ExplicitWait e) -> findElement(by)); // <3>
    }
}
