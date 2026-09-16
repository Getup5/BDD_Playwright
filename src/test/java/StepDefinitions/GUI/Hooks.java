package StepDefinitions.GUI;

import Utils.ExtentReportManager;
import Utils.LoggerUtils;
import Utils.PlaywrightManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before("@UI")
    public void setUp(Scenario scenario) {
        LoggerUtils.logInfo("========== UI Test Started: " + scenario.getName() + " ==========");

        ExtentReportManager.initializeExtentReports();
        ExtentReportManager.createTest(scenario.getName(), "Tags: " + scenario.getSourceTagNames());
        ExtentReportManager.logInfo("Test Started");

        PlaywrightManager.startScenario();
        LoggerUtils.logInfo("Browser initialized");
        ExtentReportManager.logPass("Browser initialized successfully");
    }

    @After("@UI")
    public void tearDown(Scenario scenario) {
        LoggerUtils.logInfo("========== UI Test Finished: " + scenario.getName() + " ==========");

        if (scenario.isFailed()) {
            try {
                byte[] screenshot = PlaywrightManager.takeScreenshot();
                scenario.attach(screenshot, "image/png", "Failure Screenshot");
                if (ExtentReportManager.getTest() != null) {
                    String base64 = java.util.Base64.getEncoder().encodeToString(screenshot);
                    ExtentReportManager.getTest()
                            .fail("Test FAILED: " + scenario.getName())
                            .addScreenCaptureFromBase64String(base64, "Failure Screenshot");
                }
            } catch (Exception e) {
                ExtentReportManager.logFail("Test FAILED: " + scenario.getName());
            }
        } else {
            ExtentReportManager.logPass("Test PASSED: " + scenario.getName());
        }

        PlaywrightManager.stopScenario();
        LoggerUtils.logInfo("Browser closed");
        ExtentReportManager.cleanup();
    }
}