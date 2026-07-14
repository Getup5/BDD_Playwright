package Utils;

import Context.TestContext;
import io.cucumber.datatable.DataTable;
import org.testng.Assert;
import java.util.List;
import java.util.Map;

public class ResponseUtils {

    public static void validateResponse(TestContext context, DataTable table) {
        List<Map<String, String>> data = table.asMaps(String.class, String.class);
        for (Map<String, String> row : data) {
            String locator = row.get("locator");
            String expectedValue = row.get("value");
            // if value starts with # fetch stored runtime value
            if (expectedValue.startsWith("#") && !expectedValue.equals("#should be present")) {
                expectedValue = context.getDataStore().get(expectedValue);
            }
            String actualValue = context.previousResponse.jsonPath().getString(locator);
            if (expectedValue.equals("#should be present")) {
                Assert.assertNotNull(
                    actualValue,
                    "Validation failed for JSON path : " + locator + " | Value is not present"
                );
            } else {
                Assert.assertEquals(
                    actualValue,
                    expectedValue,
                    "Validation failed for JSON path : " + locator + " | Expected : " + expectedValue + " | Actual : " + actualValue
                );
            }
        }
    }

    public static void storeVariablesValueFromResponse(TestContext context, DataTable table) {
        List<Map<String, String>> data = table.asMaps(String.class, String.class);
        for (Map<String, String> row : data) {
            String locator = row.get("locator");
            String variableName = row.get("varname");
            String value = context.previousResponse.jsonPath().getString(locator);
            context.setDataStore(variableName, value);
            LoggerUtils.logInfo( variableName + " : " + value);
        }
    }
}