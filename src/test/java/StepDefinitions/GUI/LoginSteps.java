package StepDefinitions.GUI;

import Helper.UI.Actions;
import Utils.ConfigReader;
import Utils.PlaywrightManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.microsoft.playwright.Page;

public class LoginSteps {

    private final Actions Actions;
    private final Page page = PlaywrightManager.getPage();
    public LoginSteps() {
        this.Actions = new Actions(PlaywrightManager.getPage());
    }

    @Given("user opens ui application url")
    public void userOpensUiApplicationUrl() {
        String url = ConfigReader.getProperty("VATBaseUrl");
        Actions.open(url);
    }

    @When("user enters {string} into role {string} named {string}")
    public void userEntersIntoRoleNamed(String value, String role, String visibleText) {
        Actions.fill(Actions.getByRole(AriaRole.valueOf(role.toUpperCase()), visibleText), value);
        Actions.pressKey(Actions.getByRole(AriaRole.valueOf(role.toUpperCase()), visibleText), "Tab");
    }

    @When("user clicks role {string} named {string}")
    public void userClicksRoleNamed(String role, String visibleText) {
        Actions.click(Actions.getByRole( AriaRole.valueOf(role.toUpperCase()), visibleText));
        Actions.getByLabel(visibleText).click();
    }

    @When("Verify the VAT user is able to click on create new case in TAX Invoice Delivery Maintenance")
    public void userClicksCreateNewCase() throws InterruptedException {
        Actions.getByRole(AriaRole.LINK, "Create New Case+").click();
        Actions.selectByValue(Actions.getByLabel("VAT Registration Country"), "BH");
        Actions.fill(Actions.getByCss("#vatNo"), "100001");
        Actions.getByRole(AriaRole.BUTTON, "Search").click();
       }

       @And("Verify the VAT user is able to fill all the required fields in the create new case form")
        public void userClicksCurrentDeliveryDetailsAndClose()  {
        Actions.fill(Actions.getById("primaryEmail"), "surya@gmail.com");
        Actions.fill(Actions.getById("secondaryEmail1"), "surya1@gmail.com");
        Actions.getByText("Current Delivery Details").click();
        Actions.getByRole(AriaRole.BUTTON, "Close").click();
        Actions.click(Actions.getByCss(".pemailmargin a"));
        Actions.click(Actions.getByCss(".secondaryemailmargin a"));
        Actions.uploadFile(Actions.getById("inpFileUpload"), "src/test/resources/TestFiles/File-51kb.pdf");
        Actions.click(Actions.getByRole(AriaRole.BUTTON, "Upload file"));
        Actions.assertTextContains(Actions.getByCss(".alert-success"), "File uploaded successfully");
        Actions.selectByValue(Actions.getById("mRMUser"), "checkeruser2");
        Actions.click(Actions.getByRole(AriaRole.BUTTON, "Submit"));
 }
    @Then("Verify the VAT user is able to view the confirmation message after submitting the create new case")
    public void userClicksViewCase() {
        Actions.assertTextContains(Actions.getByCss(".alert-success"), "updated successfully");
    }

}
