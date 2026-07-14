package Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReportManager — Fixed version.
 * Handles ExtentReport integration with HTML reporting.
 */
public class ExtentReportManager {

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static String reportPath;

    // ── Initialize ────────────────────────────────────────────────────────────

    public static synchronized void initializeExtentReports() {
        if (extentReports == null) {
            reportPath = "logs/reports/ExtentReport_"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"))
                    + ".html";

            // Ensure the reports directory exists
            new java.io.File("logs/reports").mkdirs();

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("Test Automation Report");
            sparkReporter.config().setReportName("Cucumber BDD Test Report");
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);

            extentReports.setSystemInfo("OS",           System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("User Name",    System.getProperty("user.name"));

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (extentReports != null) {
                    extentReports.flush();
                }
            }));

            LoggerUtils.logInfo("ExtentReport initialized at: " + reportPath);
        }
    }

    // ── Test lifecycle ────────────────────────────────────────────────────────

    public static void createTest(String testName, String description) {
        if (extentReports == null) initializeExtentReports();
        ExtentTest test = extentReports.createTest(testName, description);
        extentTest.set(test);
        LoggerUtils.logInfo("Test created in ExtentReport: " + testName);
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    // ── Logging helpers ───────────────────────────────────────────────────────

    public static void logInfo(String message) {
        if (getTest() != null) getTest().info(message);
        LoggerUtils.logInfo(message);
    }

    public static void logPass(String message) {
        if (getTest() != null) getTest().pass(message);
        LoggerUtils.logInfo("✓ " + message);
    }

    public static void logFail(String message) {
        if (getTest() != null) getTest().fail(message);
        LoggerUtils.logInfo("✗ " + message);
    }

    public static void logWarning(String message) {
        if (getTest() != null) getTest().warning(message);
        LoggerUtils.logInfo("⚠ " + message);
    }

    public static void logException(Exception exception) {
        if (getTest() != null) getTest().fail(exception);
        LoggerUtils.logInfo("ERROR: " + exception.getMessage());
    }

    // ── Flush — MUST be called from Cucumber @After hook ─────────────────────

    /**
     * Flush writes the report HTML to disk.
     * Called from BOTH Hooks classes @After method after every scenario.
     * Without this call the target/reports folder stays empty.
     */
    public static synchronized void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
            LoggerUtils.logInfo("ExtentReport flushed at: " + reportPath);
        }
    }

    // ── Cleanup ThreadLocal ───────────────────────────────────────────────────

    public static void cleanup() {
        extentTest.remove();
    }
}

