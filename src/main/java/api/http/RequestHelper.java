package api.http;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static api.spec.RequestSpecs.baseSpec;
import static io.restassured.RestAssured.given;

public class RequestHelper {

    private static RequestSpecification prepareRequest(ContentType contentType) {
        RequestSpecification request = given().spec(baseSpec);
        if (contentType != null) {
            request.contentType(contentType);
        }
        return request;
    }

    public static Response post(String path, Object body, ContentType contentType) {
        RequestSpecification request = prepareRequest(contentType);
        if (body != null) {
            request.body(body);
        }
        return request
                .when()
                .post(path);
    }

    public static Response get(String path, ContentType contentType) {
        RequestSpecification request = prepareRequest(contentType);
        return request
                .when()
                .get(path);
    }

    public static Response getWithPathParam(String path, String paramName, Object value, ContentType contentType) {
        RequestSpecification request = prepareRequest(contentType);
        if (paramName != null) {
            request.pathParam(paramName, value);
        }
        return request
                .when()
                .get(path);
    }

    public static Response getWithQueryParam(String path, String paramName, Object value, ContentType contentType) {
        RequestSpecification request = prepareRequest(contentType);
        if (paramName != null) {
            request.queryParam(paramName, value);
        }
        return request
                .when()
                .get(path);
    }

    public static Response put(String path, Object body, ContentType contentType) {
        RequestSpecification request = prepareRequest(contentType);
        if (body != null) {
            request.body(body);
        }
        return request
                .when()
                .put(path);
    }

    public static Response delete(String path, String paramName, Object value, ContentType contentType) {
        RequestSpecification request = prepareRequest(contentType);
        if (paramName != null) {
            request.pathParam(paramName, value);
        }
        return request
                .when()
                .delete(path);
    }

}
