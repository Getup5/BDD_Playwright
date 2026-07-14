package Context;

import io.restassured.response.Response;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class TestContext {

    public Response previousResponse;
    public String lastRequestUrl;
    public String lastRequestBody;
    public String lastResponseBody;
    public Integer lastResponseStatusCode;

    @Getter
    private final Map<String, String> dataStore =
            new HashMap<>();

    public void setDataStore(
            String key,
            String value) {
        dataStore.put(key, value);
    }


}