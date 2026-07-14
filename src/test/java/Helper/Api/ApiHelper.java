package Helper.Api;

import Context.TestContext;
import Utils.LoggerUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import Utils.ConfigReader;

import java.util.Map;

public class ApiHelper {

    String baseUrl = ConfigReader.getProperty("ApiBaseUrl");
    private TestContext context;

    public ApiHelper(TestContext context) {
        this.context = context;
    }

    public Response postAPI(String endpoint, String body) {
        String url = baseUrl + endpoint;

        // Store in TestContext for reporting
        if (context != null) {
            context.lastRequestUrl = url;
            context.lastRequestBody = body;
        }
        LoggerUtils.logRequestwithbody(url, body);
        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(url);

        // Store response details in TestContext
        if (context != null) {
            context.lastResponseStatusCode = response.getStatusCode();
            context.lastResponseBody = response.asPrettyString();
        }
        LoggerUtils.logResponse(response.getStatusCode(), response.asPrettyString());
        return response;
    }

    public Response postAPIWithAccessToken(String endpoint,Map<String, String> headers) {
        String url = baseUrl + endpoint;
        if (context != null) {
            context.lastRequestUrl = url;
        }
        LoggerUtils.logRequest(url);
        LoggerUtils.logInfo("\n========== REQUEST HEADERS ==========");
        headers.forEach((key, value) -> LoggerUtils.loggerInfo(key + " : " + value, key + " : " + value));
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .headers(headers)
                .when()
                .post(url);

        if (context != null) {
            context.lastResponseStatusCode = response.getStatusCode();
            context.lastResponseBody = response.asPrettyString();
        }
        LoggerUtils.logResponse(
                response.getStatusCode(),
                response.asPrettyString());
                return response;
    }
    public Response postAPIWithReqBodyAndAccessToken(String endpoint, Map<String, String> headers, String body) {

        String url = baseUrl + endpoint;
        if (context != null) {
            context.lastRequestUrl = url;
            context.lastRequestBody = body;
        }
        LoggerUtils.logRequestwithbody(url, body);
        LoggerUtils.logInfo("\n========== REQUEST HEADERS ==========");
        headers.forEach((key, value) ->
                LoggerUtils.logInfo(key + " : " + value));
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .headers(headers)
                .body(body)
                .when()
                .post(url);
        if (context != null) {
            context.lastResponseStatusCode = response.getStatusCode();
            context.lastResponseBody = response.asPrettyString();
        }
        LoggerUtils.logResponse(
                response.getStatusCode(),
                response.asPrettyString());

        return response;
    }
    public Response getAPI(String endpoint) {
        String url = baseUrl + endpoint;
        if (context != null) {
            context.lastRequestUrl = url;
            context.lastRequestBody = "GET REQUEST";
        }
        LoggerUtils.logRequest(url);
        Response response = RestAssured
                .given()
                .when()
                .get(url);
        if (context != null) {
            context.lastResponseStatusCode = response.getStatusCode();
            context.lastResponseBody = response.asPrettyString();
        }
        LoggerUtils.logResponse(response.getStatusCode(), response.asPrettyString());
        return response;
    }

    public Response putAPI(String endpoint, String body, String token) {
        String url = baseUrl + endpoint;

        if (context != null) {
            context.lastRequestUrl = url;
            context.lastRequestBody = body;
        }
        LoggerUtils.logRequest(url);
        Response response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body(body)
                .when()
                .put(url);
        if (context != null) {
            context.lastResponseStatusCode = response.getStatusCode();
            context.lastResponseBody = response.asPrettyString();
        }
        LoggerUtils.logResponse(response.getStatusCode(), response.asPrettyString());
        return response;
    }
}