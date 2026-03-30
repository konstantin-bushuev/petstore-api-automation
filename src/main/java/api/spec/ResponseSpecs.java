package api.spec;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

import static org.apache.http.HttpStatus.*;

public class ResponseSpecs {

    public static ResponseSpecification okResponse =
            new ResponseSpecBuilder()
                    .expectStatusCode(SC_OK)
                    .expectContentType(ContentType.JSON)
                    .build();

    public static ResponseSpecification notFoundResponse =
            new ResponseSpecBuilder()
                    .expectStatusCode(SC_NOT_FOUND)
                    .build();

    public static ResponseSpecification badRequestResponse =
            new ResponseSpecBuilder()
                    .expectStatusCode(SC_BAD_REQUEST)
                    .build();

    public static ResponseSpecification invalidCreateRequestResponse =
            new ResponseSpecBuilder()
                    .expectStatusCode(SC_METHOD_NOT_ALLOWED) // Petstore returns 405 for invalid create requests
                    .build();
}
