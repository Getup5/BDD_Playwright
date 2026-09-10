package StepDefinitions.GUI;

import Context.TestContext;
import Helper.UI.Actions;
import Utils.ConfigReader;
import Utils.LoggerUtils;
import Utils.WebDriverManagerUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class VatStepdef {

    private Actions Actions;
    private WebDriver driver;
    private WebDriverWait wait;
    private TestContext context;

    public VatStepdef(TestContext context) {

        this.context = context;
        driver = WebDriverManagerUtil.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        Actions = new Actions(driver);
    }

    @Given("Verify the VAT user is able to access the URL")
    public void iAmOnTheLoginPage() {
        String loginUrl = ConfigReader.getProperty("VATBaseUrl");
        WebDriverManagerUtil.navigateTo(loginUrl);
        LoggerUtils.logInfo("Navigated to VAT Portal");
    }

    @When("Verify the VAT user is able to click on create new case in TAX Invoice Delivery Maintenance")
    public void iEnterInternalAdminUsernameAndPassword() {
        Actions.click(By.xpath("//a[normalize-space(text())='Create New Case+']"));
        LoggerUtils.logInfo("User Clicked on Create New Case+ link");
    }

    @When("Verify the VAT user is able to fill all the required fields in the create new case form")
    public void iEnterRequiredFields() {
        Actions.selectDropdownByValue(By.id("vatCountry"), "BH");
        LoggerUtils.logInfo("User Selected VAT Country");
        Actions.enterText(By.id("vatNo"), "VAT-100001");
        LoggerUtils.logInfo("User Entered VAT Number");
        Actions.click(By.xpath("//input[@type='submit' and @value='Search']"));
        LoggerUtils.logInfo("User Clicked on Search button");
        Actions.waitInSeconds(15);
        Actions.enterText(By.id("primaryEmail"), "Surya@gmail.com");
        Actions.enterText(By.id("secondaryEmail1"), "Surya@verinite.com");
        Actions.selectDropdownByValue(By.id("mRMUser"), "checkeruser2");
        Actions.selectDropdownByValue(By.id("delFrquency"), "SD");
        Actions.uploadFile(By.id("inpFileUpload"), "File-51kb.pdf");
        Actions.clickUsingJS(By.xpath("//input[@type='button' and @value='Upload File']"));
        Actions.assertElementDisplayed(By.xpath("//div[contains(@class, 'alert-success') and contains(normalize-space(.), 'File uploaded successfully')]"), "File uploaded successfully");
        Actions.clickUsingJS(By.xpath("//a[normalize-space(text())='Current Delivery Details']"));
        Actions.clickVisibleCloseButtonUsingJS();
        Actions.clickUsingJS(By.xpath("//div[@class='pemailmargin']//a[normalize-space(text())='Email Linkage Details']"));
        Actions.clickVisibleCloseButtonUsingJS();
        Actions.clickUsingJS(By.xpath("//div[@class='secondaryemailmargin']//a[normalize-space(text())='Email Linkage Details']"));
        Actions.clickVisibleCloseButtonUsingJS();
        Actions.clickUsingJS(By.xpath("//input[@type='submit' and @value='Submit']"));
//        Actions.assertCaseUpdatedSuccessMessage();
        Actions.waitInSeconds(10);
    }
}