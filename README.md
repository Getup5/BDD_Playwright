# CucumberFramework

A Hybrid Test Automation Framework built with **Java**, **Cucumber BDD**, **Selenium**, and **RestAssured** — covering both UI (Web) and API test automation for the application suite

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| BDD Framework | Cucumber 7 (Gherkin) |
| UI Automation | Selenium 4 + WebDriverManager |
| API Automation | RestAssured 5 |
| Test Runner | TestNG 7 |
| Reporting | ExtentReports 5 (HTML) |
| Build Tool | Maven |
| DI Container | PicoContainer |
| Logging | Log4j2 + SLF4J |

---

## Framework Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        TEST RUNNERS                             │
│          ApiTestRunner.java       UITestRunner.java             │
└───────────────────┬─────────────────────┬───────────────────────┘
                    │                     │
                    ▼                     ▼
┌─────────────────────────────────────────────────────────────────┐
│              FEATURE FILES (.feature) — Gherkin BDD             │
│   ┌─────────────────────────┐  ┌─────────────────────────┐      │
│   │      API Features       │  │       UI Features       │      │
│   │   @API tag — no browser │  │  @UI tag — browser opens│      │
│   └─────────────────────────┘  └─────────────────────────┘      │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      STEP DEFINITIONS                           │
│  ┌──────────┐  ┌────────────┐  ┌───────────┐  ┌────────────┐    │
│  │ ApiSteps │  │ Hooks(API) │  │ LoginSteps│  │ Hooks (UI) │    │
│  │GET/POST  │  │@Before     │  │UI login   │  │browser     │    │
│  │PUT/DELETE│  │@After      │  │steps      │  │lifecycle   │    │
│  └──────────┘  └────────────┘  └───────────┘  └────────────┘    │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                          HELPERS                                │
│   ┌──────────────────────────┐  ┌──────────────────────────┐    │
│   │      ApiHelper.java      │  │      UiHelper.java       │    │
│   │  GET POST PUT DELETE     │  │  click enterText assert  │    │
│   │  PATCH + RestAssured     │  │  upload selectDropdown   │    │
│   └──────────────────────────┘  └──────────────────────────┘    │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│           TestContext.java — PicoContainer DI                   │
│   WebDriver · dataStore · API response · shared state           │
│         injected into ALL step definition classes               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                            UTILS                                │
│  ┌──────────┐ ┌──────────-┐ ┌──────────┐ ┌────────┐ ┌───────┐   │
│  │Config    │ │Driver     │ │Extent    │ │File    │ │Logger │   │
│  │Reader    │ │Manager    │ │Report    │ │Utils   │ │Utils  │   │
│  │properties│ │ThreadLocal│ │HTML      │ │JSON    │ │Log4j  │   │
│  └──────────┘ └──────────-┘ └──────────┘ └────────┘ └───────┘   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                          RESOURCES                              │
│   config.properties · JSON templates · TestData · .feature      │
└─────────────────────────────────────────────────────────────────┘
```

---

## Project Structure

```
CucumberFramework/
├── src/test/
│   ├── java/
│   │   ├── Helper/
│   │   │   ├── Api/
│   │   │   │   └── ApiHelper.java              # REST API helper (GET,POST,PUT,DELETE,PATCH)
│   │   │   └── UI/
│   │   │       └── UiHelper.java               # Selenium UI helper (click, type, assert, upload)
│   │   ├── AllRunners/
│   │   │   ├── ApiTestRunner.java              # Runs @API tagged scenarios only
│   │   │   └── UITestRunner.java               # Runs @UI tagged scenarios only
│   │   ├── StepDefinitions/
│   │   │   ├── api/
│   │   │   │   ├── ApiSteps.java               # API step definitions (GET, POST, PUT)
│   │   │   │   └── Hooks.java                  # API before/after hooks (Capture Logs, ExtentReport)
│   │   │   └── GUI/
│   │   │       ├── Login/                      # Login step definitions
│   │   │       └── Hooks.java                  # UI before/after hooks (Capture Logs, ExtentReport and Screenshot for failure cases)
│   │   ├── Context/
│   │   │   └── TestContext.java                # Shared state across steps (PicoContainer)
│   │   ├── Listeners/
│   │   │   └── TestListener.java               # TestNG listener for ExtentReport
│   │   └── Utils/
│   │       ├── ConfigReader.java               # Reads config.properties
│   │       ├── WebDriverManagerUtil.java       # ThreadLocal WebDriver management
│   │       ├── ExtentReportManager.java        # HTML report generation
│   │       ├── FileUtils.java                  # JSON template loader & updater
│   │       ├── ResponseUtils.java              # API response assertions & storage
│   │       └── LoggerUtils.java                # Console/file logging (Log4j2)
│   └── resources/
│       ├── Config/
│       │   └── config.properties               # Environment URLs, browser, test data
│       ├── Features/
│       │   ├── API/  
                └── LoginTestCases/             # API feature files
│       │           ├── LoginPositive/
│       │           ├── LoginNegative/
│       │           └── ...# API feature files
│       │   └── GUI/
│       │       └── LoginTestCases/              # UI feature files
│       │           ├── LoginPositive/
│       │           ├── LoginNegative/
│       │           └── ... UI feature files
│       ├── TestData/                           # JSON request templates + upload files
│       └── TestFiles/                          # Files used for upload scenarios
├── logs/
│   ├── classes/                                # Log4j2 log files (auto-generated)
│   └── reports/                                # ExtentReport HTML files (auto-generated)
└── pom.xml
```

---

## Prerequisites

| Tool | Version | Download |
|---|---|---|
| Java JDK | 17+ | [Download](https://www.oracle.com/java/technologies/downloads/) |
| Maven | 3.8+ | [Download](https://maven.apache.org/download.cgi) |
| Chrome Browser | Latest | Default browser |
| IntelliJ IDEA | Any | [Download](https://www.jetbrains.com/idea/download/) |
| Git | Any | [Download](https://git-scm.com/download/win) |

---

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/verinite-org/aeme-portals-test-automation.git
cd aeme-portals-test-automation
```

### 2. Install dependencies

```bash
mvn clean install -DskipTests
```

### 3. Configure environment

Edit `src/test/resources/Config/config.properties`:

```properties
# API
ApiBaseUrl=http://localhost:8080

# UI
uiBaseUrl=http://localhost:4200
browser=chrome


BaseUrl=http://localhost:4203

# Test Data
clientCode=12345
SerialNo=SN-SEED001
```

> Make sure your backend is running on port 8080 and frontend are running before executing tests.

---

## Running Tests

### Run API tests only (no browser)

```bash
mvn test -Papi
```

### Run UI tests only (browser opens)

```bash
mvn test -Pui
```

### Run all tests

```bash
mvn test -Pall
```

### Run by tag from command line

```bash
mvn test -Dcucumber.filter.tags="@Sanity"
mvn test -Dcucumber.filter.tags="@API"
mvn test -Dcucumber.filter.tags="@UI"
```

### Run from IntelliJ

- Right-click `ApiTestRunner.java` → Run (API only — no browser)
- Right-click `UITestRunner.java` → Run (UI only — browser opens)
- Right-click any `.feature` file → Run specific scenario

---

## Available Tags

| Tag | Description |
|---|---|
| `@API` | All API test scenarios |
| `@UI` | All UI test scenarios |
| `@Sanity` | Quick smoke/sanity test suite |
| `@WIP` | Work in progress scenarios |

---

## Writing a New Test

### API Test

**1. Add request template** in `src/test/resources/TestData/YourRequestTemplate.json`:

```json
{
  "field1": "value1",
  "field2": "value2"
}
```

**2. Write feature file** in `src/test/resources/Features/API/YourFeature.feature`:

```gherkin
@API @YourTag
Feature: Your API Feature

  @Sanity
  Scenario: Your scenario
    Given i have "YourAPI" api request with template "YourRequest" and following details
      | field1 | overrideValue |
    When i POST "YourEndpoint" api request
    Then the http status code should be "200"
    And the following response details should be present
      | locator | value  |
      | success | true   |
```

**3. Add endpoint** in `AmexWearableSteps.java` switch-case and `config.properties`.

---

### UI Test

**1. Write feature file** in `src/test/resources/Features/GUI/TestCases/YourFeature.feature`:

```gherkin
@UI @YourTag
Feature: Your UI Feature

  Background:
    Given Navigate to the Test Application URL
    When I enter username "admin" and password "admin"
    Then I click the login button

  @Sanity
  Scenario: Your scenario
    When your step here
    Then your assertion here
```

**2. Add step definition** in `src/test/java/StepDefinitions/GUI/YourSteps.java`:

```java
public class YourSteps {

    private final TestContext context;
    private final UiHelper uiHelper;

    public YourSteps(TestContext context) {
        this.context = context;
        this.uiHelper = new UiHelper(context.getDriver());
    }

    @When("your step here")
    public void yourStep() {
        uiHelper.click(By.xpath("//your-xpath"));
    }
}
```

---

## UiHelper — Available Methods

| Method | Description |
|---|---|
| `click(By)` | Wait and click element |
| `enterText(By, String)` | Clear and type text (char by char — Angular safe) |
| `enterTextSlow(By, String)` | Type slowly with 100ms delay per character |
| `selectDropdownByText(By, String)` | Select dropdown option by visible text |
| `selectRadioButton(By)` | Select radio if not already selected |
| `selectCheckbox(By)` | Check checkbox if not already checked |
| `selectDate(By, String)` | Enter date in date input |
| `uploadFile(By, String)` | Upload file via input (pass full path) |
| `hover(By)` | Mouse hover over element |
| `getText(By)` | Get element text |
| `isDisplayed(By)` | Check if element is visible |
| `assertElementDisplayed(By, String)` | Assert element visible + log to report |
| `assertElementText(By, String, String)` | Assert element text exact match |
| `assertElementTextContains(By, String, String)` | Assert element text contains value |
| `assertMultipleElementsDisplayed(By[], String[])` | Assert multiple elements — reports ALL failures |
| `assertSuccessMessage(By, String)` | Assert success message text |
| `assertAndAcceptAlertPopup(String)` | Verify and accept JS alert |
| `assertAndDismissAlertPopup(String)` | Verify and dismiss JS alert |
| `assertPopupDisplayed(By, String)` | Assert popup/modal visible |
| `assertPopupMessage(By, String, String)` | Assert popup message exact match |
| `handlePopup()` | Auto-dismiss any confirmation popup |
| `waitForVisible(By)` | Wait until element visible |
| `waitInSeconds(int)` | Explicit sleep (use sparingly) |

---

## Reports

After test execution, HTML reports are generated at:

```
logs/reports/ExtentReport_dd-MM-yyyy_HH-mm-ss.html
```

Open in any browser to view:

- Pass / Fail / Skip status per scenario
- Step-by-step execution details
- Screenshot on failure (UI tests only)
- Response logs (API tests)

---

## Key Design Decisions

**ThreadLocal WebDriver** — Each test thread gets its own browser instance. Prevents browser from opening during API test runs.

**UI Hooks tagged `@UI`** — `@Before("@UI")` and `@After("@UI")` ensure browser only opens/closes for scenarios tagged `@UI`. API scenarios are completely unaffected.

**PicoContainer DI** — `TestContext` is injected into every step definition class automatically via constructor injection — no static variables needed for sharing state between steps.

**Template-based API requests** — JSON request bodies are stored as templates. DataTable overrides specific fields per test scenario — keeping tests clean and readable.

**Lazy WebDriver init** — `TestContext.getDriver()` only creates the browser on first call. Pure API scenarios never open a browser window.

**ConfigReader** — All environment values (URLs, credentials, browser) are read from `config.properties` — no hardcoded values in test code.

**Relative XPaths** — All locators anchored to Angular component tags (e.g. `//app-user//input[@id='username']`) — resilient to layout changes.

**Char-by-char typing** — `enterText()` uses `Actions` with 50ms delay per character — ensures Angular `ng-dirty` / `ng-valid` bindings update correctly.

---

## Troubleshooting

**`git` not recognized in PowerShell**

```bash
$env:PATH += ";C:\Program Files\Git\cmd"
```

Or add `C:\Program Files\Git\cmd` to System Environment Variables → PATH permanently.

---

**Browser opens during API tests**

Ensure `@Before("@UI")` annotation is used in `StepDefinitions/GUI/Hooks.java` — not plain `@Before()`.

---

**ExtentReport is empty / not generated**

Ensure `ExtentReportManager.flushReport()` is called in both `@After` hooks (UI and API).

---

**CDP version warning on Chrome 149**

```
WARNING: Unable to find CDP implementation matching 149
```

This is harmless — tests run fine. Suppressed via `log4j2.xml` and `logging.properties`. No action needed.

---

**Port already in use**

```bash
# Find and kill port (Windows)
netstat -ano | findstr :4203
taskkill /PID <PID> /F

# Kill all node processes (kills all Angular dev servers)
taskkill /IM node.exe /F

# Kill all Angular ports at once
for %p in (4200 4202 4203 4204 4205 4206 4207 4208 4209) do (
  for /f "tokens=5" %a in ('netstat -ano ^| findstr :%p') do taskkill /PID %a /F
)
```

---

**Undo a git pull (revert to before pull)**

```bash
git reset --hard ORIG_HEAD
```

Safe — only affects your local copy, never touches remote or main branch.

---

## Contributing

```bash
# 1. Create a new branch
git checkout -b feature/your-feature

# 2. Make changes and commit
git commit -m "Add your feature"

# 3. Push
git push origin feature/your-feature

# 4. Open a Pull Request on GitHub
```

> All PRs must be reviewed and approved by the repository maintainer before merging to main.