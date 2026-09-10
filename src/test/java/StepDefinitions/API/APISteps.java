package StepDefinitions.API;

import Helper.Api.ApiHelper;
import Context.TestContext;
import Utils.LoggerUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import Utils.ConfigReader;
import Utils.FileUtils;
import Utils.ResponseUtils;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class APISteps {

    TestContext context;
    ApiHelper apiHelper;
    FileUtils fileUtils;

    public APISteps(TestContext context) {
        this.context = context;
        this.apiHelper = new ApiHelper(context);
        this.fileUtils = new FileUtils();
    }

    @Given("i have {string} api request with template {string} and following details")
    public void iApiRequestWithTemplateAndFollowingDetails(String apiName, String templateName, DataTable table) throws Exception {
        Map<String, String> data = table.asMap(String.class, String.class);
        String jsonRequest = fileUtils.getDataFromTemplate(templateName + "RequestTemplate.json");
        context.setDataStore("requestJson", fileUtils.updateDataWithScenarioDataTableInfo(context, data, jsonRequest));
    }

    @Given("i have {string} api request with template {string}")
    public void iApiRequestWithTemplate(String apiName, String templateName) throws Exception {
        String jsonRequest = fileUtils.getDataFromTemplate(templateName + "RequestTemplate.json");
        context.setDataStore("requestJson", jsonRequest);
    }

    @When("i POST {string} api request")
    public void iPOSTApiRequest(String apiName) {
        Response response = null;
        String requestJson = context.getDataStore().get("requestJson");

        switch (apiName) {
            case "AuthToken":
                response = apiHelper.postAPI(ConfigReader.getProperty("authEndpoint"), requestJson);
                break;
            case "CreateVatLoginEndpoint":
                response = apiHelper.postAPI(ConfigReader.getProperty("CreateVatLoginEndpoint"), requestJson);
                break;
            default:
                throw new RuntimeException("POST API NOT FOUND");
        }

        context.previousResponse = response;
    }

    @When("i GET {string} api request")
    public void iGETApiRequest(String apiName) {
        Response response = null;

        switch (apiName) {
            case "GetClientData":
                response = apiHelper.getAPI(ConfigReader.getProperty("getClientData") + ConfigReader.getProperty("clientCode"));
                break;
            default:
                throw new RuntimeException("GET API NOT FOUND");
        }

        context.previousResponse = response;
    }

    @When("i PUT {string} api request")
    public void iPUTApiRequest(String apiName) {
        Response response = null;
        String requestJson = context.getDataStore().get("requestJson");

        switch (apiName) {
            case "UpdateBooking":
                response = apiHelper.putAPI(ConfigReader.getProperty("updateBookingEndpoint") + context.getDataStore().get("BookingId"), requestJson, context.getDataStore().get("Token"));
                break;
            default:
                throw new RuntimeException("PUT API NOT FOUND");
        }
        context.previousResponse = response;
    }

    @Then("the http status code should be {string}")
    public void theHttpStatusCodeShouldBe(String statusCode) {
        String actualStatusCode = Integer.toString(context.previousResponse.getStatusCode());
        assertEquals(actualStatusCode, statusCode);
    }

    @And("the following response details should be present")
    public void validateResponseDetails(DataTable table) {
        ResponseUtils.validateResponse(context, table);
    }

    @And("i have following values from the transaction")
    public void iHaveFollowingValuesFromTheTransaction(DataTable table) {
        ResponseUtils.storeVariablesValueFromResponse(context, table);
        // Verify that values are stored in the datastore
        List<Map<String, String>> data = table.asMaps(String.class, String.class);
        for (Map<String, String> row : data) {
            String varName = row.get("varname");
            String storedValue = context.getDataStore().get(varName);
            if (storedValue != null && !storedValue.isEmpty()) {
                LoggerUtils.logInfo("✓ Variable '" + varName + "' successfully stored with value: " + storedValue);
            } else {
                throw new AssertionError("Variable '" + varName + "' was not stored in datastore!");
            }
        }
    }
}