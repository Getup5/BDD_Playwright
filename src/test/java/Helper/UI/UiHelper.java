package Helper.UI;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import Utils.ExtentReportManager;
import Utils.LoggerUtils;

import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class UiHelper {

    private WebDriver driver;
    private WebDriverWait wait;

    public UiHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }
    public void enterText(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }
    public void enterTextSlowly(By locator, String value) {
        try {
            WebElement element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();

            Actions actions = new Actions(driver);
            for (char c : value.toCharArray()) {
                actions.sendKeys(element, String.valueOf(c))
                        .pause(Duration.ofMillis(50))   // ← getShortWait() equivalent
                        .perform();
            }
            LoggerUtils.logInfo("✓ Entered text: " + value);
        } catch (Exception e) {
            LoggerUtils.logInfo("✗ Enter text failed: " + e.getMessage());
            fail("✗ Enter text failed: " + e.getMessage());
        }
    }

    public void selectMultipleValues(By locator, String... values) {
        Select select = new Select(
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator)));
        for (String value : values) {
        select.selectByVisibleText(value);
    }}

    public String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    public void selectDropdownByText(By locator, String value) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(locator)));

        select.selectByVisibleText(value);
    }

    public void selectDropdownByValue(By locator, String value) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(locator)));

        select.selectByValue(value);
    }

    public void selectRadioButton(By locator) {
        WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(locator));

        if (!radio.isSelected()) {
            radio.click();
        }
    }

    public void selectCheckbox(By locator) {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(locator));

        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void selectDate(By locator, String date) {
        WebElement dateField = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        dateField.clear();
        dateField.sendKeys(date);
    }

    public void hover(By locator) {
        Actions actions = new Actions(driver);
        actions.moveToElement(wait.until(ExpectedConditions.visibilityOfElementLocated(locator))).perform();
    }

    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public boolean isDisplayed(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
    }

    public void waitInSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void handlePopup() {
        try {
            try {
                Thread.sleep(100);
                driver.switchTo().alert().accept();
                LoggerUtils.logInfo("Accepted alert popup");
                return;
            } catch (Exception e) {
            }
            By confirmButton = By.xpath("//button[contains(text(), 'OK') or contains(text(), 'Confirm') or contains(text(), 'Yes')]");
            if (isPopupElementPresent(confirmButton)) {
                click(confirmButton);
                LoggerUtils.logInfo("Clicked confirmation button on popup");
                return;
            }
            Thread.sleep(100);
            LoggerUtils.logInfo("Download popup handled - proceeding");

        } catch (Exception e) {
            LoggerUtils.logInfo("Error handling popup: " + e.getMessage());
        }
    }

    public void PasswordAlertHandle(){
        try {
            try {
                click(By.xpath("//button[contains(text(), 'OK') or contains(text(), 'Never') or contains(text(), 'Skip')]"));
                LoggerUtils.logInfo("Dismissed password manager popup");
            } catch (Exception e) {
                driver.switchTo().alert().dismiss();
                LoggerUtils.logInfo("Dismissed password manager alert");
            }
        } catch (Exception e) {
            LoggerUtils.logInfo("No popup to dismiss");
        }
    }

    private boolean isPopupElementPresent(By locator) {
        try {
            Thread.sleep(500);
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * Captures screenshot and attaches it to ExtentReport
     * Called automatically when assertions fail
     */
    private void captureScreenshotOnFailure(String failureContext) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            if (ExtentReportManager.getTest() != null) {
                String base64 = Base64.getEncoder().encodeToString(screenshot);
                ExtentReportManager.getTest().addScreenCaptureFromBase64String(base64, "Failure Screenshot - " + failureContext);
                LoggerUtils.logInfo("Screenshot captured for failure: " + failureContext);
            }
        } catch (Exception e) {
            LoggerUtils.logInfo("Failed to capture screenshot: " + e.getMessage());
        }
    }
    // ========== ASSERTION HELPER METHODS ==========
    /**
     * Assert that an element is displayed on the page
     */
    public void assertElementDisplayed(By locator, String elementName) {
        try {
            boolean isDisplayed = isDisplayed(locator);
            assertTrue(isDisplayed, elementName + " is not displayed on the page");
            String message = "✓ " + elementName + " is successfully displayed";
            LoggerUtils.logInfo(message);
            ExtentReportManager.logPass(message);
        } catch (Exception e) {
            String errorMessage = "✗ " + elementName + " is NOT displayed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure(elementName + " - Not Displayed");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }
    /**
     * Assert that an element's text equals the expected value
     */
    public void assertElementText(By locator, String expectedText, String elementName) {
        try {
            String actualText = getText(locator).trim();
            assertEquals(actualText, expectedText, elementName + " text does not match");
            String message = "✓ " + elementName + " displays correct text: '" + expectedText + "'";
            LoggerUtils.logInfo(message);
            ExtentReportManager.logPass(message);
        } catch (Exception e) {
            String errorMessage = "✗ " + elementName + " text assertion failed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure(elementName + " - Text Mismatch");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }

    public void assertElementTextContains(By locator, String expectedText, String elementName) {
        try {
            String actualText = getText(locator);
            assertTrue(actualText.contains(expectedText), elementName + " does not contain expected text: '" + expectedText + "'");
            String message = "✓ " + elementName + " contains text: '" + expectedText + "'";
            LoggerUtils.logInfo(message);
            ExtentReportManager.logPass(message);
        } catch (Exception e) {
            String errorMessage = "✗ " + elementName + " text contains assertion failed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure(elementName + " - Text Not Found");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }

    public void assertSuccessMessage(By locator, String expectedMessage) {
        try {
            WebElement element = waitForVisible(locator);
            String actualMessage = element.getText().trim();
            assertTrue(!actualMessage.isEmpty(), "Success message is empty");
            assertTrue(actualMessage.contains(expectedMessage) || actualMessage.equals(expectedMessage), "Success message does not match. Expected: '" + expectedMessage + "' Actual: '" + actualMessage + "'");
            String successMessage = "✓ SUCCESS MESSAGE VERIFIED: " + actualMessage;
            LoggerUtils.logInfo(successMessage);
            ExtentReportManager.logPass(successMessage);
        } catch (Exception e) {
            String errorMessage = "✗ SUCCESS MESSAGE NOT FOUND - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure("Success Message Not Found");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }

    public void assertMultipleElementsDisplayed(By[] locators, String[] elementNames) {
        try {
            assertEquals(locators.length, elementNames.length, "Locators and element names count must match");

            for (int i = 0; i < locators.length; i++) {
                boolean isDisplayed = isDisplayed(locators[i]);
                assertTrue(isDisplayed, elementNames[i] + " is not displayed");
            }
            String message = "✓ All " + locators.length + " elements are successfully displayed";
            LoggerUtils.logInfo(message);
            ExtentReportManager.logPass(message);
        } catch (Exception e) {
            String errorMessage = "✗ Multiple elements assertion failed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure("Multiple Elements Not Displayed");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }

    public void assertElementNotDisplayed(By locator, String elementName) {
        try {
            boolean isDisplayed = false;
            try {
                isDisplayed = isDisplayed(locator);
            } catch (Exception e) {
                isDisplayed = false;
            }
            assertFalse(isDisplayed, elementName + " should not be displayed");
            String message = "✓ " + elementName + " is successfully hidden/not displayed";
            LoggerUtils.logInfo(message);
            ExtentReportManager.logPass(message);
        } catch (Exception e) {
            String errorMessage = "✗ " + elementName + " is still displayed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure(elementName + " - Still Displayed");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }

    }

    public void assertPopupDisplayed(By popupLocator, String elementName) {
        try {
            WebElement popup = waitForVisible(popupLocator);
            assertTrue(popup.isDisplayed(), elementName + " popup is not displayed");
            String message = "✓ " + elementName + " popup is successfully displayed";
            LoggerUtils.logInfo(message);
            ExtentReportManager.logPass(message);
        } catch (Exception e) {
            String errorMessage = "✗ " + elementName + " popup is NOT displayed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure(elementName + " Popup - Not Displayed");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }

    public void assertPopupMessage(By popupLocator, String expectedMessage, String elementName) {
        try {
            String actualMessage = getText(popupLocator).trim();
            assertEquals(actualMessage, expectedMessage, elementName + " popup message does not match");
            String message = "✓ " + elementName + " popup displays correct message: '" + expectedMessage + "'";
            LoggerUtils.logInfo(message);
            ExtentReportManager.logPass(message);
        } catch (Exception e) {
            String errorMessage = "✗ " + elementName + " popup message assertion failed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure(elementName + " Popup - Message Mismatch");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }

    public void assertPopupMessageContains(By popupLocator, String expectedText, String elementName) {
        try {
            String actualMessage = getText(popupLocator).trim();
            assertTrue(actualMessage.contains(expectedText), elementName + " popup does not contain expected text: '" + expectedText + "'");
            String message = "✓ " + elementName + " popup contains message: '" + expectedText + "'";
            LoggerUtils.logInfo(message);
            ExtentReportManager.logPass(message);
        } catch (Exception e) {
            String errorMessage = "✗ " + elementName + " popup text contains assertion failed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure(elementName + " Popup - Text Not Found");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }

    public void assertAndAcceptAlertPopup(String expectedMessage) {

        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String actualMessage = alert.getText().trim();
            assertEquals(actualMessage, expectedMessage, "Alert message DOES NOT MATCH. Expected: '" + expectedMessage + "' Actual: '" + actualMessage + "'");
            alert.accept();
            String message = "✓ Alert popup verified and accepted. Message: '" + actualMessage + "'";
            ExtentReportManager.logPass(message);
        } catch (AssertionError e) {
            String errorMessage = "✗ Alert assertion failed - " + e.getMessage();
            captureScreenshotOnFailure("Alert Popup - Text Mismatch");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        } catch (Exception e) {
            String errorMessage = "✗ Alert handling failed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure("Alert Popup - Error");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }
    public void assertAndAcceptAlertPopupContains(String expectedMessage) {

        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String actualMessage = alert.getText().trim();
            assertTrue(actualMessage.contains(expectedMessage), "Alert message DOES NOT CONTAIN expected text. Expected substring: '" + expectedMessage + "' Actual: '" + actualMessage + "'");
            alert.accept();
            String message = "✓ Alert popup verified (contains '" + expectedMessage + "') and accepted. Full message: '" + actualMessage + "'";
            LoggerUtils.logInfo(message);
            ExtentReportManager.logPass(message);
        } catch (AssertionError e) {
            String errorMessage = "✗ Alert assertion failed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure("Alert Popup - Text Not Found");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        } catch (Exception e) {
            String errorMessage = "✗ Alert handling failed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure("Alert Popup - Error");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }

    public void assertAndDismissAlertPopup(String expectedMessage) {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String actualMessage = alert.getText().trim();
            assertEquals(actualMessage, expectedMessage, "Alert message DOES NOT MATCH. Expected: '" + expectedMessage + "' Actual: '" + actualMessage + "'");
            alert.dismiss();
            String message = "✓ Alert popup verified and dismissed. Message: '" + actualMessage + "'";
            ExtentReportManager.logPass(message);
        } catch (AssertionError e) {
            String errorMessage = "✗ Alert assertion failed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure("Alert Popup - Text Mismatch");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        } catch (Exception e) {
            String errorMessage = "✗ Alert dismiss assertion failed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure("Alert Popup - Error");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }
    public void assertAndDismissAlertPopupContains(String expectedMessage) {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String actualMessage = alert.getText().trim();
            assertTrue(actualMessage.contains(expectedMessage) || actualMessage.equals(expectedMessage), "Alert message DOES NOT CONTAIN expected text. Expected substring: '" + expectedMessage + "' Actual: '" + actualMessage + "'");
            alert.dismiss();
            String message = "✓ Alert popup verified (contains '" + expectedMessage + "') and dismissed. Full message: '" + actualMessage + "'";
            LoggerUtils.logInfo(message);
            ExtentReportManager.logPass(message);
        } catch (AssertionError e) {
            String errorMessage = "✗ Alert assertion failed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure("Alert Popup - Text Not Found");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        } catch (Exception e) {
            String errorMessage = "✗ Alert dismiss assertion failed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure("Alert Popup - Error");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }
    public void assertPopupAndClose(By popupLocator, String expectedMessage, By closeButtonLocator, String elementName) {
        try {
            String actualMessage = getText(popupLocator).trim();
            assertTrue(actualMessage.contains(expectedMessage) || actualMessage.equals(expectedMessage), elementName + " message mismatch. Expected: '" + expectedMessage + "' Actual: '" + actualMessage + "'");
            click(closeButtonLocator);
            String message = "✓ " + elementName + " popup verified and closed. Message: '" + actualMessage + "'";
            LoggerUtils.logInfo(message);
            ExtentReportManager.logPass(message);
        } catch (Exception e) {
            String errorMessage = "✗ " + elementName + " popup assertion/close failed - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure(elementName + " Popup - Assertion/Close Failed");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }

    public void assertPopupDisappears(By popupLocator, String elementName) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(popupLocator));
            String message = "✓ " + elementName + " popup has successfully disappeared";
            LoggerUtils.logInfo(message);
            ExtentReportManager.logPass(message);
        } catch (Exception e) {
            String errorMessage = "✗ " + elementName + " popup did not disappear - " + e.getMessage();
            LoggerUtils.logInfo(errorMessage);
            captureScreenshotOnFailure(elementName + " Popup - Still Visible");
            ExtentReportManager.logFail(errorMessage);
            fail(errorMessage);
        }
    }

    public void uploadFile(By locator, String filePath) {
        try {
            // Build full absolute path
            String absolutePath = System.getProperty("user.dir")
                    + "\\src\\test\\resources\\TestFiles\\" + filePath;

            WebElement fileInput = driver.findElement(locator);
            fileInput.sendKeys(absolutePath);

            LoggerUtils.logInfo("✓ File uploaded: " + absolutePath);
            ExtentReportManager.logPass("✓ File uploaded: " + absolutePath);
        } catch (Exception e) {
            LoggerUtils.logInfo("✗ File upload failed: " + e.getMessage());
            captureScreenshotOnFailure("File Upload Failed");
            ExtentReportManager.logFail("✗ File upload failed: " + e.getMessage());
            fail("✗ File upload failed: " + e.getMessage());
        }
    }
}
