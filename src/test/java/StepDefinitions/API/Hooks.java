package StepDefinitions.API;

import Context.TestContext;
import Utils.ExtentReportManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import Utils.LoggerUtils;

/**
 * Hooks class for API Test Cases
 * Handles ExtentReport integration (no browser operations - API tests only)
 * Logs request, response, and URL details to the report
 */
public class Hooks {

    private TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before("@API")
    public void setUp(Scenario scenario) {
        LoggerUtils.logInfo("========== API Test Started: " + scenario.getName() + " ==========");

        ExtentReportManager.initializeExtentReports();
        ExtentReportManager.createTest(scenario.getName(), "Tags: " + scenario.getSourceTagNames());
        ExtentReportManager.logInfo("API Test Started");
        LoggerUtils.logInfo("API Test initialized");
    }

    @After("@API")
    public void tearDown(Scenario scenario) {
        LoggerUtils.logInfo("========== API Test Finished: " + scenario.getName() + " ==========");

        // Log API details to ExtentReport
        if (context != null) {
            if (context.lastRequestUrl != null) {
                ExtentReportManager.logInfo("<b>REQUEST URL:</b> " + context.lastRequestUrl);
            }
            if (context.lastRequestBody != null) {
                ExtentReportManager.logInfo("<b>REQUEST BODY:</b> <pre>" + context.lastRequestBody + "</pre>");
            }
            if (context.lastResponseStatusCode != null) {
                ExtentReportManager.logInfo("<b>RESPONSE STATUS CODE:</b> " + context.lastResponseStatusCode);
            }
            if (context.lastResponseBody != null) {
                ExtentReportManager.logInfo("<b>RESPONSE BODY:</b> <pre>" + context.lastResponseBody + "</pre>");
            }
        }

        // Log test result to ExtentReport
        if (scenario.isFailed()) {
            ExtentReportManager.logFail("Test FAILED: " + scenario.getName());
        } else {
            ExtentReportManager.logPass("Test PASSED: " + scenario.getName());
        }

        LoggerUtils.logInfo("API Test cleanup completed");

        // Flush and cleanup
        ExtentReportManager.flushReport();
        ExtentReportManager.cleanup();
        LoggerUtils.clearContext();  // Clear MDC context
    }
}

