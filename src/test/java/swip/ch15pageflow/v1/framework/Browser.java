package swip.ch15pageflow.v1.framework;


import com.google.common.base.Predicate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Browser extends DelegatingSearchContext<WebDriver> implements WebDriver {

    public Browser(WebDriver driver) {
        super(driver);
    }

    @Override
    public void get(String url) {
        delegate.get(url);
    }

    @Override
    public String getCurrentUrl() {
        return delegate.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return delegate.getTitle();
    }

    @Override
    public String getPageSource() {
        return delegate.getPageSource();
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public void quit() {
        delegate.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return delegate.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return delegate.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return delegate.switchTo();
    }

    @Override
    public Navigation navigate() {
        return delegate.navigate();
    }

    @Override
    public Options manage() {
        return delegate.manage();
    }
    public void setInputText(Supplier<By> by, String value) {
        Retry retry = new Retry(5, 1, TimeUnit.SECONDS);

        retry.attempt(
            new Attemptable() {
                @Override
                public void attempt() throws Exception {
                    Element element = findElement(by);
                    element.clear();
                    element.sendKeys(value);
                    assert value.equals(element.getAttribute("value"));
                }
            }
        );
    }

    public void setInputTextLambda(Supplier<By> by, String value) {
        Retry retry = new Retry(5, 1, TimeUnit.SECONDS);

        retry.attempt(
            () -> {
                Element element = findElement(by);
                element.clear();
                element.sendKeys(value);
                assert value.equals(element.getAttribute("value"));
            }
        );
    }

    public String getInputText(Supplier<By> by) {
        return untilFound(by).getAttribute("value");
    }

    public void setCheckboxValue(Supplier<By> by, boolean value) {
        Element checkbox = untilFound(by);
        if (checkbox.isSelected() != value) {
            checkbox.click();
        }
    }

    public boolean isChecked(Supplier<By> by) {
        return untilFound(by).isSelected();
    }

    public void setRadio(Supplier<By> by, String value) {
        List<WebElement> radiobuttons = findElements(by.get());

        assert radiobuttons.size() >= 2;

        for (WebElement e : radiobuttons) {
            if (e.getAttribute("value").equals(value)) {
                e.click();
                return;
            }
        }
        throw new IllegalArgumentException(
            "unable to find element with value " + value);
    }

    public String getRadio(Supplier<By> by) {
        List<WebElement> radiobuttons = findElements(by.get());

        assert radiobuttons.size() >= 2;

        for (WebElement e : radiobuttons) {
            if (Boolean.valueOf(e.getAttribute("checked"))) {
                return e.getAttribute("value");
            }
        }
        return null;
    }

    public Select getSelect(Supplier<By> by) {
        final Element element = untilFound(by);
        new WebDriverWait(this, 3, 100)
            .until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver driver) {
                    element.click();
                    return !element.findElements(By.tagName("option")).isEmpty();
                }
            });
        return new Select(element);
    }

    public void select(Supplier<By> by, Object select) {
        getSelect(by).selectByVisibleText(select.toString());
        try {
            if (!getSelect(by)
                .getFirstSelectedOption()
                .getText()
                .equals(select.toString())) {
                getSelect(by)
                    .getOptions()
                    .stream()
                    .filter(
                        (WebElement e) ->
                            e.getText().equals(select.toString()))
                    .findFirst()
                    .get()
                    .click();
            }
        } catch (Exception e) {
            //Don't need to handle it.
        }
    }

    public Select getSelectLambda(Supplier<By> by) {
        Element element = untilFound(by);
        new WebDriverWait(this, 3, 100)
            .until((WebDriver driver) -> {
                element.click();
                return !element.findElements(By.tagName("option")).isEmpty();
            });
        return new Select(element);
    }
}