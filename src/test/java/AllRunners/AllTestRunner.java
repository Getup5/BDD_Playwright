package AllRunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {
                "src/test/resources/Features/API",
                "src/test/resources/Features/GUI"
        },
        glue = {
                "StepDefinitions.API",
                "StepDefinitions.GUI"
        },
        tags = "(@API or @UI) and not @WIP",
        objectFactory = io.cucumber.picocontainer.PicoFactory.class,
        plugin = {
                "pretty",
                "html:target/combined-cucumber-report.html",
                "json:target/combined-cucumber.json",
                "junit:target/combined-cucumber.xml"
        }
)
public class AllTestRunner extends AbstractTestNGCucumberTests {
}