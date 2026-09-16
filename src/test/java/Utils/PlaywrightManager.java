package Utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightManager {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    private PlaywrightManager() {
    }

    public static synchronized Page getPage() {
        if (page == null) {
            startScenario();
        }
        return page;
    }

    public static synchronized void startScenario() {
        if (playwright == null) {
            playwright = Playwright.create();
        }
        if (browser == null || !browser.isConnected()) {
            browser = launchConfiguredBrowser();
        }
        if (context == null) {
            context = browser.newContext();
            page = context.newPage();
        }
    }

    private static Browser launchConfiguredBrowser() {
        String browserName = (ConfigReader.getProperty("browser") == null)
                ? "chromium"
                : ConfigReader.getProperty("browser").trim().toLowerCase();

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(Boolean.parseBoolean(ConfigReader.getProperty("headless")));

        switch (browserName) {
            case "chrome":
                options.setChannel("chrome");
                return playwright.chromium().launch(options);
            case "edge":
                options.setChannel("msedge");
                return playwright.chromium().launch(options);
            case "chromium":
                return playwright.chromium().launch(options);
            case "firefox":
                return playwright.firefox().launch(options);
            case "webkit":
                return playwright.webkit().launch(options);
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserName + ". Use chrome, edge, chromium, firefox, or webkit.");
        }
    }

    public static synchronized void stopScenario() {
        if (page != null) {
            page.close();
            page = null;
        }
        if (context != null) {
            context.close();
            context = null;
        }
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    public static byte[] takeScreenshot() {
        return getPage().screenshot();
    }
}
