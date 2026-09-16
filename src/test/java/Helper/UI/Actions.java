package Helper.UI;

import Utils.LoggerUtils;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import Utils.ExtentReportManager;

public class Actions {

    private final Page page;

    public Actions(Page page) {
        this.page = page;
    }
    // =========================================================================
    // NAVIGATION
    // =========================================================================

    public void open(String url) {
        page.navigate(url);
        LoggerUtils.logInfo("Opened URL: " + url);
    }

    // =========================================================================
    // FIND ELEMENTS
    // =========================================================================

    public Locator getByRole(AriaRole role, String name) {
        return page.getByRole(role, new Page.GetByRoleOptions().setName(name));
    }

    public Locator getByLabel(String label) {
        return page.getByLabel(label);
    }

    public Locator getByPlaceholder(String placeholder) {
        return page.getByPlaceholder(placeholder);
    }

    public Locator getById(String id) {
        return page.locator("#" + id);
    }

    public Locator getByText(String text) {
        return page.getByText(text);
    }

    public Locator getByCss(String cssSelector) {
        return page.locator(cssSelector);
    }

    public void click(Locator locator) {
        locator.click();
        LoggerUtils.logInfo("Clicked: " + locator);
    }

    public void doubleClick(Locator locator) {
        locator.dblclick();
        LoggerUtils.logInfo("Double-clicked: " + locator);
    }

    public void fill(Locator locator, String value) {
        locator.fill(value);
        LoggerUtils.logInfo("Filled value into: " + locator);
    }

    public void typeSlowly(Locator locator, String value) {
        locator.pressSequentially(value);
        LoggerUtils.logInfo("Typed (key-by-key) into: " + locator);
    }

    public void clear(Locator locator) {
        locator.clear();
        LoggerUtils.logInfo("Cleared: " + locator);
    }

    //Radio button
    public void check(Locator locator) {
        locator.check();
        locator.getByRole(AriaRole.RADIO).check();
        LoggerUtils.logInfo("Checked: " + locator);
    }

    public void uncheck(Locator locator) {
        locator.uncheck();
        LoggerUtils.logInfo("Unchecked: " + locator);
    }

    public void selectByLabel(Locator locator, String visibleOptionText) {
        locator.selectOption(new SelectOption().setLabel(visibleOptionText));
        LoggerUtils.logInfo("Selected option (by label) '" + visibleOptionText + "' on: " + locator);
    }

    public void selectByValue(Locator locator, String optionValue) {
        locator.selectOption(new SelectOption().setValue(optionValue));
        LoggerUtils.logInfo("Selected option (by value) '" + optionValue + "' on: " + locator);
    }

    public void hover(Locator locator) {
        locator.hover();
        LoggerUtils.logInfo("Hovered: " + locator);
    }

    public void pressKey(Locator locator, String key) {
        locator.press(key);
        LoggerUtils.logInfo("Pressed key '" + key + "' on: " + locator);
    }

    public void dragAndDrop(Locator source, Locator target) {
        source.dragTo(target);
        LoggerUtils.logInfo("Dragged " + source + " onto " + target);
    }

    public void uploadFile(Locator locator, String filePath) {
        locator.setInputFiles(Paths.get(filePath));
        LoggerUtils.logInfo("Uploaded file '" + filePath + "' to: " + locator);
    }

    // =========================================================================
    // READ VALUES (no assertion — just returns current state)
    // =========================================================================

    public String getText(Locator locator) {
        return locator.textContent();
    }

    public String getValue(Locator locator) {
        return locator.inputValue();
    }

    // =========================================================================
    // VERIFY SOMETHING (all auto-retrying — safe against timing/loading issues)
    // =========================================================================

    public void assertVisible(Locator locator) {
        try {
            assertThat(locator).isVisible();
            ExtentReportManager.logPass("Assert Visible");
        } catch (AssertionError | RuntimeException e) {
            ExtentReportManager.logFail("Assert Visible failed - " + e.getMessage());
            throw e;
        }
    }

    public void assertHidden(Locator locator) {
        try {
            assertThat(locator).isHidden();
            ExtentReportManager.logPass("Assert Hidden");
        } catch (AssertionError | RuntimeException e) {
            ExtentReportManager.logFail("Assert Hidden failed - " + e.getMessage());
            throw e;
        }
    }

    public void assertEnabled(Locator locator) {
        try {
            assertThat(locator).isEnabled();
            ExtentReportManager.logPass("Assert Enabled");
        } catch (AssertionError | RuntimeException e) {
            ExtentReportManager.logFail("Assert Enabled failed - " + e.getMessage());
            throw e;
        }
    }

    public void assertDisabled(Locator locator) {
        try {
            assertThat(locator).isDisabled();
            ExtentReportManager.logPass("Assert Disabled");
        } catch (AssertionError | RuntimeException e) {
            ExtentReportManager.logFail("Assert Disabled failed - " + e.getMessage());
            throw e;
        }
    }

    public void assertChecked(Locator locator) {
        try {
            assertThat(locator).isChecked();
            ExtentReportManager.logPass("Assert Checked");
        } catch (AssertionError | RuntimeException e) {
            ExtentReportManager.logFail("Assert Checked failed - " + e.getMessage());
            throw e;
        }
    }

    public void assertTextEquals(Locator locator, String expectedText) {
        try {
            assertThat(locator).hasText(expectedText);
            String actual = locator.textContent();
            ExtentReportManager.logPass("Assert Text Equals: expected='" + expectedText + "', actual='" + (actual == null ? "" : actual) + "'");
        } catch (AssertionError | RuntimeException e) {
            String actual = "";
            try { actual = locator.textContent(); } catch (Exception ignore) {}
            ExtentReportManager.logFail("Assert Text Equals failed - expected='" + expectedText + "', actual='" + actual + "' - " + e.getMessage());
            throw e;
        }
    }

    public void assertTextContains(Locator locator, String expectedSubstring) {
        try {
            assertThat(locator).containsText(expectedSubstring);
            String actual = locator.textContent();
            ExtentReportManager.logPass("Assert Text Contains: expectedSubstring='" + expectedSubstring + "', actual='" + (actual == null ? "" : actual) + "'");
        } catch (AssertionError | RuntimeException e) {
            String actual = "";
            try { actual = locator.textContent(); } catch (Exception ignore) {}
            ExtentReportManager.logFail("Assert Text Contains failed - expectedSubstring='" + expectedSubstring + "', actual='" + actual + "' - " + e.getMessage());
            throw e;
        }
    }

    public void assertValueEquals(Locator locator, String expectedValue) {
        try {
            assertThat(locator).hasValue(expectedValue);
            String actual = locator.inputValue();
            ExtentReportManager.logPass("Assert Value Equals: expected='" + expectedValue + "', actual='" + (actual == null ? "" : actual) + "'");
        } catch (AssertionError | RuntimeException e) {
            String actual = "";
            try { actual = locator.inputValue(); } catch (Exception ignore) {}
            ExtentReportManager.logFail("Assert Value Equals failed - expected='" + expectedValue + "', actual='" + actual + "' - " + e.getMessage());
            throw e;
        }
    }

    public void assertValueContains(Locator locator, String expectedSubstring) {
        try {
            assertThat(locator).hasValue(Pattern.compile(".*" + Pattern.quote(expectedSubstring) + ".*"));
            String actual = locator.inputValue();
            ExtentReportManager.logPass("Assert Value Contains: expectedSubstring='" + expectedSubstring + "', actual='" + (actual == null ? "" : actual) + "'");
        } catch (AssertionError | RuntimeException e) {
            String actual = "";
            try { actual = locator.inputValue(); } catch (Exception ignore) {}
            ExtentReportManager.logFail("Assert Value Contains failed - expectedSubstring='" + expectedSubstring + "', actual='" + actual + "' - " + e.getMessage());
            throw e;
        }
    }
}