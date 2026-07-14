package AllRunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/Features/API",
        glue = "StepDefinitions.api",
        tags = "(@Positive or @Negative) and not @WIP",
        objectFactory = io.cucumber.picocontainer.PicoFactory.class,
        plugin = {
                "pretty",
                "html:target/api-cucumber-report.html",
                "json:target/api-cucumber.json",
                "junit:target/api-cucumber.xml"
        }
)
public class ApiTestRunner extends AbstractTestNGCucumberTests {
}